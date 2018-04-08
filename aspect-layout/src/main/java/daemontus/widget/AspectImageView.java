package daemontus.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * An ImageView with a constrained aspect ratio.
 *
 * @author Vincent Mimoun-Prat @ MarvinLabs (www.marvinlabs.com)
 */
public class AspectImageView extends AppCompatImageView implements AspectRatioDelegate.ConstrainedView {

    public AspectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        aspectRatioDelegate = new AspectRatioDelegate(this, attrs);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        aspectRatioDelegate = new AspectRatioDelegate(this, attrs);
    }

    public AspectImageView(Context context) {
        super(context);
        aspectRatioDelegate = new AspectRatioDelegate(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        aspectRatioDelegate.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("WrongCall")
    @Override
    public void callParentOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private final AspectRatioDelegate aspectRatioDelegate;
}