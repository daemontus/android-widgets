package daemontus.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

/**
 * SwapImageButton extends the implementation of SwapLayout to provide a way to
 * create stateful image buttons with parcelable state objects.
 */
public class SwapImageButton extends SwapLayout implements SwapLayout.SwappedOut {

    private LruCache<State, View> stateCache = new LruCache<>(10);

    @Nullable
    private State state;

    public SwapImageButton(@NonNull Context context) {
        super(context);
    }

    public SwapImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwapImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onSwappedOut(@NonNull View child) {
        if (child.getTag() != null && child.getTag() instanceof State) {
            State state = (State) child.getTag();
            stateCache.put(state, child);
        }
    }

    @Nullable
    public State getState() {
        return state;
    }

    public void setState(@Nullable State state) {
        if (this.state == state) return;
        if (this.state != null && this.state.equals(state)) return;

        View child = null;
        if (state != null) {
            child = stateCache.get(state);
            if (child == null) {
                child = newViewForState(state);
            }
        }
        this.state = state;
        swapChild(child, this);
    }

    @NonNull
    protected View newViewForState(State state) {
        ImageView child = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        child.setImageResource(state.src);
        child.setBackgroundResource(state.background);
        child.setContentDescription(getContext().getString(state.contentDescription));
        return child;
    }

    /**
     * State is a Parcelable representation of current button status.
     *
     * It is immutable and implements standard semantic equivalence relation.
     *
     * Ideally, you should create a state singleton for each of your states, and then
     * simply use those.
     */
    public static class State implements Parcelable {

        @DrawableRes
        private final int src;

        @DrawableRes
        private final int background;

        @StringRes
        private final int contentDescription;

        public State(
                @DrawableRes int src,
                @DrawableRes int background,
                @StringRes int contentDescription
        ) {
            this.src = src;
            this.background = background;
            this.contentDescription = contentDescription;
        }

        State(Parcel in) {
            src = in.readInt();
            background = in.readInt();
            contentDescription = in.readInt();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;

            State state = (State) o;

            return
                    src == state.src &&
                            background == state.background &&
                            contentDescription == state.contentDescription
                    ;
        }

        @Override
        public int hashCode() {
            int result = src;
            result = 31 * result + background;
            result = 31 * result + contentDescription;
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(src);
            parcel.writeInt(background);
            parcel.writeInt(contentDescription);
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return new State(in);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

    }

}
