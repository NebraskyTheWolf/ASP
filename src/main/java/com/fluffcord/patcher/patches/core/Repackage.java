package com.fluffcord.patcher.patches.core;

import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.ManifestUtil;

import java.io.File;
import java.io.IOException;

public class Repackage {
    public static void patch(File root, IOCache cache) throws IOException {
        File manifestFile = new File(root, "AndroidManifest.xml");
        String manifest = cache.readString(manifestFile);
        manifest = manifest.replace("package=\"com.discord\"", "package=\"com.fluffcord.app\"");
        manifest = manifest.replace(" android:permission=\"com.discord.permission.CONNECT\"", "");
        manifest = manifest.replace("com.discord.permission.CONNECT", "com.fluffcord.app.permission.CONNECT");
        manifest = manifest.replace("android:authorities=\"com.discord.", "android:authorities=\"com.fluffcord.app.");
        manifest = manifest.replace("android:label=\"@string/discord\"", "android:label=\"FluffCord\"");
        manifest = ManifestUtil.usePermission(manifest, "com.google.android.c2dm.permission.RECEIVE");
        manifest = ensureIntent(manifest, "com.discord.intent.action.SDK");
        manifest = ensureIntent(manifest, "com.discord.intent.action.CONNECT");
        manifest = ensureIntent(manifest, "com.discord.intent.action.ENQUEUE_WORK");
        manifest = ensureIntent(manifest, "com.discord.intent.action.NOTIFICATION_DELETED");
        manifest = ensureIntent(manifest, "com.discord.intent.action.NOTIFICATION_CANCEL");
        // Temporary
        manifest = manifest.replace("@mipmap/ic_logo_round", "@mipmap/ic_logo_round_debug");
        manifest = manifest.replace("@mipmap/ic_logo_square", "@mipmap/ic_logo_square_debug");


        cache.writeString(manifestFile, manifest);
    }

    private static String ensureIntent(String manifest, String intent) {
        String intentFrom = "<action android:name=\""+intent+"\"/>";
        return manifest.replace(intentFrom, intentFrom +
                (intentFrom.replace("com.discord.", "com.fluffcord.app.")));
    }
}
