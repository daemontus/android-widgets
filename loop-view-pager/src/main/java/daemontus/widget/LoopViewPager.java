package daemontus.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * A ViewPager which loops through it's items forever. It works by using a special
 * adapter which adds a duplicate of the last item to the front and duplicate of the
 * first item to the back of the adapter, then switching seamlessly from front to
 * back when necessary.
 *
 * However, you should never need to care about this, because all wrapping/changes
 * should happen on background. The only difference is that now, certain pages can be
 * instantiated multiple times at the same time.
 */
public class LoopViewPager extends ViewPager {

    @Nullable
    private LoopAdapter adapterWrapper;

    private Map<OnPageChangeListener, LoopPageChangeListener> pageChangeMap = new HashMap<>();
    private Map<OnAdapterChangeListener, LoopAdapterChangeListener> adapterChangeMap = new HashMap<>();

    public LoopViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /* Switch page to it's duplicate at the back of the view pager. */
    private Runnable goToEnd = new Runnable() {
        @Override
        public void run() {
            if (LoopViewPager.super.getCurrentItem() == 0) {
                LoopViewPager.super.setCurrentItem(getRealCount(), false);
            }
        }
    };

    /* Switch page to it's duplicate at the front of the view pager. */
    private Runnable goToStart = new Runnable() {
        @Override
        public void run() {
            if (LoopViewPager.super.getCurrentItem() == getRealCount() + 1) {
                LoopViewPager.super.setCurrentItem(1, false);
            }
        }
    };

    private void init() {
        // Add a special page change listener which will automatically switch
        // between pages as you flip them.
        super.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                removeCallbacks(goToEnd);   // cancel any pending page changes
                removeCallbacks(goToStart);
                if (position == 0) {
                    postDelayed(goToEnd, 200);
                }
                if (position == getRealCount() + 1) {
                    postDelayed(goToStart, 200);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /* Fancy hacks to hide the wrapper adapter from end user. */

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        LoopPageChangeListener listenerWrapper = new LoopPageChangeListener(listener);
        pageChangeMap.put(listener, listenerWrapper);
        super.addOnPageChangeListener(listenerWrapper);
    }

    @Override
    public void removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.removeOnPageChangeListener(pageChangeMap.get(listener));
    }

    @Override
    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        LoopAdapterChangeListener listenerWrapper = new LoopAdapterChangeListener(listener);
        adapterChangeMap.put(listener, listenerWrapper);
        super.addOnAdapterChangeListener(listenerWrapper);
    }

    @Override
    public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        super.removeOnAdapterChangeListener(adapterChangeMap.get(listener));
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (adapter == null) {
            adapterWrapper = null;
            super.setAdapter(null);
        } else {
            adapterWrapper = new LoopAdapter(adapter);
            super.setAdapter(adapterWrapper);
            if (adapter.getCount() > 0) {   // first item is actually the last one
                super.setCurrentItem(1);
            }
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item+1);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item+1, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return getRealPosition(super.getCurrentItem());
    }

    @Nullable
    @Override
    public PagerAdapter getAdapter() {
        PagerAdapter adapter = super.getAdapter();
        if (adapter == null) {
            return null;
        } else {
            return ((LoopAdapter) adapter).innerAdapter;
        }
    }

    private int getRealCount() {
        if (adapterWrapper != null) {
            return adapterWrapper.getRealCount();
        } else {
            return 0;
        }
    }

    private int getRealPosition(int loopPosition) {
        int realCount = getRealCount();
        if (loopPosition == 0) {
            return realCount - 1;
        } else if (loopPosition == realCount + 1) {
            return 0;
        } else {
            return loopPosition - 1;
        }
    }

    /**
     * A wrapper around a pager adapter
     */
    private class LoopAdapter extends PagerAdapter {

        private final PagerAdapter innerAdapter;

        private LoopAdapter(PagerAdapter innerAdapter) {
            this.innerAdapter = innerAdapter;
        }

        int getRealCount() {
            return innerAdapter.getCount();
        }

        @Override
        public int getCount() {
            int realCount = innerAdapter.getCount();
            if (realCount == 0) {
                return 0;
            } else {
                return realCount + 2;
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return innerAdapter.isViewFromObject(view, object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return innerAdapter.instantiateItem(container, getRealPosition(position));
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            innerAdapter.destroyItem(container, getRealPosition(position), object);
        }

        @Override
        public void registerDataSetObserver(@NonNull DataSetObserver observer) {
            innerAdapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
            innerAdapter.unregisterDataSetObserver(observer);
        }

        @Nullable
        @Override
        public Parcelable saveState() {
            return innerAdapter.saveState();
        }

        @Override
        public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
            innerAdapter.restoreState(state, loader);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            int innerPosition = innerAdapter.getItemPosition(object);
            if (innerPosition >= 0) innerPosition += 1;
            return innerPosition;
        }

        @Override
        public float getPageWidth(int position) {
            return innerAdapter.getPageWidth(getRealPosition(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return innerAdapter.getPageTitle(getRealPosition(position));
        }
    }

    /**
     * A wrapper around page change listener which will re-calculate item position
     */
    private class LoopPageChangeListener implements OnPageChangeListener {

        private final OnPageChangeListener innerListener;

        private LoopPageChangeListener(OnPageChangeListener innerListener) {
            this.innerListener = innerListener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            innerListener.onPageScrolled(getRealPosition(position), positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            innerListener.onPageSelected(getRealPosition(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            innerListener.onPageScrollStateChanged(state);
        }

    }

    /**
     * Unwrap adapters for the listener when adapter change happens.
     */
    private static class LoopAdapterChangeListener implements OnAdapterChangeListener {

        private final OnAdapterChangeListener innerListener;

        private LoopAdapterChangeListener(OnAdapterChangeListener innerListener) {
            this.innerListener = innerListener;
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            PagerAdapter oldInner = null;
            if (oldAdapter != null) {
                oldInner = ((LoopAdapter) oldAdapter).innerAdapter;
            }
            PagerAdapter newInner = null;
            if (newAdapter != null) {
                newInner = ((LoopAdapter) newAdapter).innerAdapter;
            }
            this.innerListener.onAdapterChanged(viewPager, oldInner, newInner);
        }

    }
}
