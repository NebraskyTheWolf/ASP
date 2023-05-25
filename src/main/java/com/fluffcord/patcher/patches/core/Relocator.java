package com.fluffcord.patcher.patches.core;

import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliHelper;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Relocator {
    public static void patch(File root, IOCache cache) throws IOException {
        File smali = new File(root, "smali");
        relocate(smali, "android" + File.separator + "support");
    }

    private static void relocate(File file,String root) throws IOException {
        relocate0(new File(file, root), root);
    }

    private static void relocate0(File file,String root) throws IOException {
        if (!file.exists()) return;
        for (File f: Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) {
                relocate0(f, root + File.separator + f.getName());
            } else if (f.getName().endsWith(".smali")) {
                SmaliHelper.relocate(root + File.separator + f.getName());
            }
        }
    }
}
