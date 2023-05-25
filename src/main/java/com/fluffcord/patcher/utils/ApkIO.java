package com.fluffcord.patcher.utils;

import brut.androlib.Androlib;
import brut.androlib.ApkDecoder;
import brut.androlib.ApkOptions;
import brut.common.BrutException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ApkIO {
    public static void disassemble(File apk, File dir) throws BrutException, IOException {
        ApkDecoder decoder = new ApkDecoder();
        // decoder.setKeepBrokenResources(true);
        decoder.setApkFile(apk);
        decoder.setOutDir(dir);
        decoder.decode();
    }

    public static void assemble(File dir, File apk) throws BrutException, IOException {
        ApkOptions apkOptions = new ApkOptions();
        // apkOptions.useAapt2 = true;
        new Androlib(apkOptions).build(dir, apk);
    }

    public static void delete(File f) throws IOException {
        if (!f.exists()) {
            return;
        }
        if (f.isDirectory()) {
            for (File c : Objects.requireNonNull(f.listFiles())) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new IOException("Failed to delete file: " + f);
        }
    }

    public static void mkdirs(File f) throws IOException {
        if (f.isDirectory()) {
            return;
        }
        if (f.isFile()) {
            if (!f.delete()) {
                throw new IOException("Failed to delete file: " + f);
            }
        }
        if (!f.mkdirs()) {
            throw new IOException("Failed to create dir: " + f);
        }
    }
}
