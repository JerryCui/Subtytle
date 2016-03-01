package com.peike.theatersubtitle.util;

import android.content.Context;


public class ResourceUtil {
    public static int getCountryFlagResId(Context context, String iso639) {
        String resourceName = Constants.PREFIX_RES_FLAG + iso639;
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }
}