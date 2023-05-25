package com.fluffcord.patcher.patches.core;

import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliClass;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreHooks {
    public static void patch(File root, IOCache cache) throws IOException {
        SmaliClass app = new SmaliClass(cache.readSmali("com/discord/app/App.smali"));
        app.getMethod("onCreate").prepend(Collections.singletonList(
                "invoke-static {p0}, Lcom/fluffcord/app/FluffCord;->init(Landroid/app/Application;)V"));
        cache.writeSmali("com/discord/app/App.smali", app.getSmali());
        File widget_settings_file = new File(root, "res/layout/widget_settings.xml");
        List<String> widget_settings = cache.readLines(widget_settings_file);
        for (int i = 0; i < widget_settings.size();i++) {
            String line = widget_settings.get(i);
            if (line.contains("developer")) {
                widget_settings.set(i, line.replace(" android:visibility=\"gone\"", ""));
            } else if (line.contains("android:id=\"@id/settings_upload_debug_logs\"")){
                widget_settings.set(i, line.replace("android:id=\"@id/settings_upload_debug_logs\"",
                        "android:id=\"@id/settings_upload_debug_logs\" android:visibility=\"gone\""));
            } else if (line.contains("android:id=\"@id/settings_language\"")) {
                widget_settings.addAll(i+1, Arrays.asList(
                        "<View style=\"@style/UiKit.Settings.Divider\" />",
                        "<TextView android:text=\"Fluff Cord\" style=\"@style/UiKit.Settings.Item.Header\" />",
                        setting("Modules", "@drawable/ic_round_extension_24dp"),
                        setting("App info", "@drawable/ic_round_extension_24dp")
                ));
                widget_settings.addAll(i+1, Arrays.asList(
                        "<View style=\"@style/UiKit.Settings.Divider\" />",
                        "<TextView android:text=\"UI\" style=\"@style/UiKit.Settings.Item.Header\" />",
                        setting("Themes", "@drawable/ic_round_extension_24dp"),
                        setting("Animation", "@drawable/ic_round_extension_24dp")
                ));
                widget_settings.addAll(i+1, Arrays.asList(
                        "<View style=\"@style/UiKit.Settings.Divider\" />",
                        "<TextView android:text=\"Tweaks\" style=\"@style/UiKit.Settings.Item.Header\" />",
                        setting("Injector", "@drawable/ic_round_extension_24dp"),
                        setting("User Flags", "@drawable/ic_round_extension_24dp")
                ));
            }
        }
        cache.writeLines(widget_settings_file, widget_settings);
        File manifestFile = new File(root, "AndroidManifest.xml");
        List<String> manifest = cache.readLines(manifestFile);
        int appClose = -1;
        for (int i = 0; i < manifest.size();i++) {
            if (manifest.get(i).trim().equals("</application>")) {
                appClose = i;
                break;
            }
        }
        manifest.add(appClose, activity("com.fluffcord.app.settings.FluffSettingActivity"));
        cache.writeLines(manifestFile, manifest);
    }

    @SuppressWarnings("SameParameterValue")
    private static String setting(String text, String drawable) {
        return "<com.fluffcord.app.settings.DiscordSettingItem android:text=\""+text+"\" " +
                "android:drawableStart=\""+drawable+"\" " +
                "app:drawableTint=\"?colorInteractiveNormal\" " +
                "style=\"@style/UiKit.Settings.Item.Icon\" />";
    }

    @SuppressWarnings("SameParameterValue")
    private static String activity(String cl) {
        return "<activity android:name=\"" + cl + "\" " +
                "android:screenOrientation=\"fullUser\" " +
                "android:windowSoftInputMode=\"adjustResize|stateHidden\" " +
                "android:autoRemoveFromRecents=\"true\" />";
    }
}
