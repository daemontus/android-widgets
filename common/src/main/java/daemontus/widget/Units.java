package daemontus.widget;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Basic unit conversions.
 */
public class Units {

    float dpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    float pxToDp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

}
