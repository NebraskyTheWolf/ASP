package com.fluffcord.patcher.patches.etc;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.ApkIO;
import com.fluffcord.patcher.utils.smali.SmaliClass;
import com.fluffcord.patcher.utils.smali.SmaliHelper;
import com.fluffcord.patcher.utils.smali.SmaliMethod;
import com.fluffcord.patcher.utils.smali.SmaliOpcodes;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CrashAnalyticsDisabler implements SmaliOpcodes {

    public static void patch(File file, IOCache cache) {
        try {
            File file1 = new File(file, "lib");
            for (File sub: Objects.requireNonNull(file1.listFiles())) {
                sub = new File(sub, "libcrashlytics.so").getAbsoluteFile();
                ApkIO.delete(sub);
            }
        } catch (Exception e) {
            Pawtcher.LOGGER.error("Error removing libcrashlytics", e);
        }
        try {
            File manifestFile = new File(file, "AndroidManifest.xml");
            List<String> lines = cache.readLines(manifestFile);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.contains("lytics")) continue;
                int len = line.length();
                line = line.replace("=\"true\"", "=\"false\"");
                if (len == line.length()) {
                    line = "";
                }
                lines.set(i, line);
            }
        } catch (Exception e) {
            Pawtcher.LOGGER.error("Error patching AndroidManifest.xml", e);
        }
        try {
            File FirebaseCrashlytics = SmaliHelper.resolveSmali("com/google/firebase/crashlytics/FirebaseCrashlytics.smali");
            if (FirebaseCrashlytics != null) {
                SmaliClass smaliClass = new SmaliClass(cache.readLines(FirebaseCrashlytics));
                SmaliMethod checkForUnsentReports = smaliClass.getMethod("checkForUnsentReports");
                SmaliMethod deleteUnsentReports = smaliClass.getMethod("deleteUnsentReports()V");
                if (checkForUnsentReports.exists() && deleteUnsentReports.exists()) {
                    checkForUnsentReports.prepend(Collections.singletonList(
                            "invoke-virtual {p0}, Lcom/google/firebase/crashlytics/FirebaseCrashlytics;->deleteUnsentReports()V"
                    ));
                }
                SmaliMethod didCrashOnPreviousExecution = smaliClass.getMethod("didCrashOnPreviousExecution()Z");
                if (didCrashOnPreviousExecution.exists()) {
                    didCrashOnPreviousExecution.set(Arrays.asList(
                            ".locals 1",
                            "const/4 v0, 0x0",
                            "return v0"
                    ));
                }
                cache.writeLines(FirebaseCrashlytics, smaliClass.getSmali());
            }
        } catch (Exception e) {
            Pawtcher.LOGGER.error("Error patching FirebaseCrashlytics", e);
        }
    }
}
