package com.fluffcord.patcher.utils.json;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.utils.ApkIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class JsonHelper {
    private static final HashMap<String, File> cache = new HashMap<>();
    private static final ArrayList<File> jsonRoots = new ArrayList<>();

    public static void init(File root) throws IOException {
        cache.clear();
        jsonRoots.clear();
        for (File file: Objects.requireNonNull(root.listFiles())) {
            if (file.isDirectory() &&
                    (file.getName().equals("assets"))) {
                jsonRoots.add(file);

                Pawtcher.LOGGER.skip(file.getAbsolutePath());
            }
        }
        if (jsonRoots.isEmpty()) {
            File file = new File(root, "data");
            ApkIO.mkdirs(file);
            jsonRoots.add(file);
        }
    }

    public static File resolveJson(String json) {
        File resolved = cache.get(json);
        if (resolved != null) {
            return resolved;
        }
        for (File root:jsonRoots) {
            resolved = new File(root, json);
            if (resolved.isFile()) {
                cache.put(json, resolved);
                return resolved;
            }
        }
        return null;
    }
}
