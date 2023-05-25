package com.fluffcord.patcher.cache;

import com.fluffcord.patcher.utils.smali.SmaliHelper;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IOCache {
    private final HashMap<String, Reference<byte[]>> cache;

    public IOCache() {
        this.cache = new HashMap<>();
    }

    public byte[] readBytes(File file) throws IOException {
        return this.readBytes(file, false);
    }

    public byte[] readBytesOnce(File file) throws IOException {
        return this.readBytes(file, true);
    }

    public byte[] readBytes(File file,boolean once) throws IOException {
        String path = file.getAbsolutePath();
        Reference<byte[]> ref = cache.get(path);
        byte[] data = ref == null ? null : ref.get();
        if (data == null) {
            data = Files.readAllBytes(file.toPath());
            this.cache.put(path, once ?
                    new WeakReference<>(data) :
                    new SoftReference<>(data));
        } else if (!once && ref instanceof WeakReference){
            this.cache.put(path, new SoftReference<>(data));
        }
        return data;
    }

    public void writeBytes(File file,byte[] data) throws IOException {
        this.writeBytes(file, data, false);
    }

    public void writeBytesOnce(File file,byte[] data) throws IOException {
        this.writeBytes(file, data, true);
    }

    public void writeBytes(File file,byte[] data,boolean once) throws IOException {
        String path = file.getAbsolutePath();
        this.cache.put(path, once ?
                new WeakReference<>(data) :
                new SoftReference<>(data));
        Files.write(file.toPath(), data);
    }

    public InputStream asInputSteam(File file) throws IOException {
        return this.asInputSteam(file, false);
    }

    public InputStream asInputSteam(File file,boolean once) throws IOException {
        return new IOCacheInputStream(this.readBytes(file, once));
    }

    public OutputStream asOutputStream(File file) throws IOException {
        return this.asOutputStream(file, false);
    }

    public OutputStream asOutputStream(File file, boolean once) throws IOException {
        return new IOCacheOutputStream(this, file, once);
    }

    public String readString(File file) throws IOException {
        return new String(this.readBytes(file), StandardCharsets.UTF_8);
    }

    public void writeString(File file,String data) throws IOException {
        this.writeBytes(file, data.getBytes(StandardCharsets.UTF_8));
    }

    public List<String> readLines(File file) throws IOException {
        return new LinkedList<>(Arrays.asList(this.readString(file).split("\\r?\\n")));
    }

    public void writeLines(File file, List<String> lines) throws IOException {
        PrintStream printStream = new PrintStream(this.asOutputStream(file));
        for (String line:lines) printStream.println(line);
        printStream.close();
    }

    public List<String> readSmali(String smali) throws IOException {
        return this.readLines(SmaliHelper.resolveSmali(smali));
    }

    public void writeSmali(String smali, List<String> lines) throws IOException {
        this.writeLines(SmaliHelper.resolveSmali(smali), lines);
    }

    public void clear() {
        this.cache.clear();
    }
}
