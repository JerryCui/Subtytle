package com.peike.theatersubtitle.util;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackUtil {
   public static void show(View view, String message) {
       Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
   }
}
