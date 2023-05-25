package com.fluffcord.patcher.utils;

public class ManifestUtil {
    private static final String USE_INTERNET_PERM = "<uses-permission android:name=\"android.permission.INTERNET\"/>";

    public static String usePermission(String manifest,String permission) {
        String usePermission = "<uses-permission android:name=\"" + permission + "\"/>";
        if (manifest.contains(usePermission)) {
            return manifest;
        }
        return manifest.replace(USE_INTERNET_PERM,
                USE_INTERNET_PERM + "\n    " + usePermission);
    }
}
