package com.peike.theatersubtitle.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    /**
     * Hides the device's Keyboard if open
     *
     * @param view - A view that may have gotten focus and opened the Keypad
     */
    public static void hideSoftKeyPad(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Shows the device's Keyboard
     *
     * @param view - A view that may have gotten focus and opened the Keypad
     */
    public static void showSoftKeyPad(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Toggles the device's Keyboard. Shows the keyboard if it was not visible, hides it if it was visible.
     *
     * @param view - A view that may have gotten focus and opened the Keypad
     */
    public static void toggleSoftKeyPad(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view.requestFocus()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
