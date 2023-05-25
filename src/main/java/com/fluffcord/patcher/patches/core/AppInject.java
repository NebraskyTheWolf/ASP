package com.fluffcord.patcher.patches.core;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.ApkIO;
import com.fluffcord.patcher.utils.ZipEntryInputStream;
import com.fluffcord.patcher.utils.smali.SmaliHelper;

import org.jf.baksmali.Baksmali;
import org.jf.baksmali.BaksmaliOptions;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AppInject {
    private static final DexOptions dexOptions;
    private static final CfOptions cfOptions;
    private static final Opcodes opcodes;
    private static final BaksmaliOptions baksmaliOptions;
    private static final Set<String> keep = new HashSet<>(Arrays.asList(
            "com/discord/KeepA.class",
            "com/discord/KeepB.class",
            "com/discord/KeepC.class"
    ));

    static {
        final int apiLevel = 13;
        dexOptions = new DexOptions();
        dexOptions.targetApiLevel = apiLevel;
        cfOptions = new CfOptions();
        cfOptions.optimize = true;
        cfOptions.strictNameCheck = false;
        opcodes = Opcodes.forApi(apiLevel);
        baksmaliOptions = new BaksmaliOptions();
        baksmaliOptions.apiLevel = apiLevel;
        baksmaliOptions.localsDirective = true;
    }

    public static void patch(File root, IOCache cache) throws IOException {
        InputStream inputStream = AppInject.class.getResourceAsStream("/App.jar");
        if (inputStream == null) {
            File root_root = root.getParentFile();
            if (root_root.getName().equals("run")) {
                String sep = File.separator;
                File app = new File(root_root.getParentFile(), "App" + sep + "build" + sep
                        + "outputs" + sep + "aar" + sep + "App-debug.aar").getAbsoluteFile();
                if (app.exists()) {
                    inputStream = new FileInputStream(app);
                }
            }
        }
        if (inputStream == null) {
            throw new FileNotFoundException("Unable to find App.jar");
        }
        extractDex(aarToDex(inputStream,
                root, cache), SmaliHelper.getNewSmaliDir());

        for (String cl:keep) {
            SmaliHelper.relocate(cl.replace(".class", ".smali"));
        }
    }

    private static void extractDex(byte[] dex,File output) throws IOException {
        DexBackedDexFile dexBackedDexFile = new DexBackedDexFile(opcodes, dex);
        Baksmali.disassembleDexFile(dexBackedDexFile, output, 4, baksmaliOptions);
    }

    private static byte[] aarToDex(InputStream jar,File root, IOCache cache) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(jar);
        ZipEntry zipEntry;
        byte[] dex = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory() && zipEntry.getName().indexOf('/') != -1) {
                File dest = new File(root, zipEntry.getName()).getAbsoluteFile();
                ApkIO.mkdirs(dest.getParentFile());
                FileOutputStream fos = new FileOutputStream(dest);
                byte[] buffer = new byte[8192];
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fos.close();
                continue;
            }
            if (zipEntry.getName().equals("classes.jar")) {
                dex = jarToDex(new ZipEntryInputStream(zipInputStream), root, cache);
            }
        }
        zipInputStream.close();
        if (dex == null) {
            throw new FileNotFoundException("/classes.jar doesn't exists!");
        }
        return dex;
    }

    private static byte[] jarToDex(InputStream jar,File root, IOCache cache) throws IOException {
        DexFile outputDex = new DexFile(dexOptions);
        ZipInputStream zipInputStream = new ZipInputStream(jar);
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory() && (zipEntry.getName().startsWith("res/") ||
                    zipEntry.getName().startsWith("assets/"))) {
                File dest = new File(root, zipEntry.getName()).getAbsoluteFile();
                ApkIO.mkdirs(dest.getParentFile());
                FileOutputStream fos = new FileOutputStream(dest);
                byte[] buffer = new byte[8192];
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                fos.close();
                continue;
            }
            if (!zipEntry.getName().endsWith(".class") ||
                    ((zipEntry.getName().startsWith("com/discord/") ||
                            zipEntry.getName().startsWith(
                                    "com/google/android/material/") ||
                            zipEntry.getName().startsWith("androidx/")
                    ) && !keep.contains(zipEntry.getName()))) continue;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            outputDex.add(createClassDef(zipEntry.getName(), baos.toByteArray()));
        }
        zipInputStream.close();
        return outputDex.toDex(null, false);
    }

    private static ClassDefItem createClassDef(String name,byte[] bytecode) {
        return CfTranslator.translate(name, bytecode, cfOptions, dexOptions);
    }
}
