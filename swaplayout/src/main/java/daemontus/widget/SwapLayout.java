package daemontus.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

/**
 * SwapLayout is designed to always show at most one view. Each view swap is
 * animated. Additionally, when no view is displayed, the whole layout is hidden.
 *
 * WARNING: Don't use addView/removeView methods to edit the contents of the layout, things
 * will break. Only use the swapChild method to change views.
 *
 * If you want to modify the behaviour of default animations, override
 * onInit, onAppear, onDisappear, onSwapIn or onSwapOut.
 */
public class SwapLayout extends FrameLayout {

    private static Interpolator INTERPOLATOR_IN = new LinearOutSlowInInterpolator();
    private static Interpolator INTERPOLATOR_OUT = new FastOutLinearInInterpolator();
    private static final long DURATION_DISAPPEAR = 195;
    private static final long DURATION_APPEAR = 115;
    private static final long DURATION_SWAP = 200;

    public SwapLayout(@NonNull Context context) {
        super(context);
        onInit();
    }

    public SwapLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public SwapLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    @Nullable
    private View mainView;

    public void swapChild(@Nullable View child) {
        swapChild(child, null);
    }

    public void swapChild(@Nullable View child, @Nullable SwappedOut callback) {
        if (child != null && mainView == null) {
            // Add a new view into an empty layout.
            onAppear(child);
            mainView = child;
            return;
        }
        if (child == null && mainView != null) {
            // Remove view from a non empty layout.
            onDisappear(mainView, callback);
            mainView = null;
            return;
        }
        if (child != null && child != mainView) {
            // Swap views.
            onSwapIn(child);
            onSwapOut(mainView, callback);
            mainView = child;
        }
        // Else: child has not changed.
    }

    protected void onInit() {
        if (getChildCount() == 0) {
            setScaleX(0f);
            setScaleY(0f);
            setAlpha(0f);
        }
    }

    protected void onAppear(@NonNull View child) {
        addView(child);
        this.animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(DURATION_APPEAR)
                .setInterpolator(INTERPOLATOR_IN)
                ;
    }

    protected void onDisappear(@NonNull final View child, @Nullable final SwappedOut callback) {
        this.animate()
                .scaleX(0f).scaleY(0f).alpha(0f)
                .setDuration(DURATION_DISAPPEAR)
                .setInterpolator(INTERPOLATOR_OUT)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(child);
                        animate().setListener(null);
                        if (callback != null) callback.onSwappedOut(child);
                    }
                })
                ;
    }

    protected void onSwapIn(@NonNull View child) {
        addView(child);
        child.animate().cancel();
        child.setAlpha(0f);
        child.setTranslationY(shiftDistance());
        child.animate()
                .translationY(0f).alpha(1f)
                .setDuration(DURATION_SWAP)
                .setInterpolator(INTERPOLATOR_OUT)
                ;

    }

    protected void onSwapOut(@NonNull final View child, @Nullable final SwappedOut callback) {
        child.animate().cancel();
        child.animate()
                .translationY(-shiftDistance()).alpha(0f)
                .setDuration(DURATION_SWAP)
                .setInterpolator(INTERPOLATOR_OUT)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeView(child);
                        child.setTranslationY(0f);
                        child.setAlpha(1f);
                        child.animate().setListener(null);
                        if (callback != null) callback.onSwappedOut(child);
                    }
                })
                ;
    }

    private float shiftDistance() {
        return getHeight() / 2f;
    }

    public interface SwappedOut {
        void onSwappedOut(@NonNull View child);
    }

}
