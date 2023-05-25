package com.fluffcord.app.utils;

import android.content.Intent;
import android.os.Build;

public class IntentUtils {
    public static void removeFlags(Intent intent,int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.removeFlags(flags);
        } else {
            intent.setFlags(intent.getFlags() & ~flags);
        }
    }
}
