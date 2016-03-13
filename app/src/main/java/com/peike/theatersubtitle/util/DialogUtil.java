package com.peike.theatersubtitle.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {
    public static void confirm(Context context, String message, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message)
                .setPositiveButton(android.R.string.yes, callback)
                .setNegativeButton(android.R.string.no, callback)
                .show();
    }
}
