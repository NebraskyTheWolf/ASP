package com.fluffcord.patcher.utils;

import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class ZipEntryInputStream extends FilterInputStream implements Closeable {
    private final ZipInputStream zis;

    public ZipEntryInputStream(ZipInputStream inputStream) {
        super(inputStream);
        this.zis = inputStream;
    }

    @Override
    public void close() throws IOException {
        zis.closeEntry();
    }
}
