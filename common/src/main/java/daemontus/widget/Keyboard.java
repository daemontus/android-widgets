package daemontus.widget;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Allows easily showing/hiding software keyboard.
 */
public class Keyboard {

    public static void hide(Context context, View window) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(window.getWindowToken(), 0);
        }
    }

    public static void show(Context context, View target) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(target, 0);
        }
    }

}
