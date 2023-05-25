package com.fluffcord.patcher.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;

final class IOCacheOutputStream extends FilterOutputStream {
    private IOCache ioCache;
    private final File file;
    private final boolean once;
    private boolean dirty;

    IOCacheOutputStream(IOCache ioCache, File file,boolean once) {
        super(new ByteArrayOutputStream());
        this.ioCache = ioCache;
        this.file = file;
        this.once = once;
        this.dirty = true;
    }

    @Override
    public void write(int b) throws IOException {
        if (out == null) {
            throw new IOException("Stream Closed");
        }
        out.write(b);
        dirty = true;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (out == null) {
            throw new IOException("Stream Closed");
        }
        out.write(b, off, len);
        dirty = true;
    }

    @Override
    public void flush() throws IOException {
        if (out == null) {
            throw new IOException("Stream Closed");
        }
        if (dirty) {
            ioCache.writeBytes(file,
                    ((ByteArrayOutputStream) out).toByteArray(),
                    this.once);
            dirty = false;
        }
    }

    @Override
    public void close() throws IOException {
        this.flush();
        // Clear reference if closed
        this.out = null;
        this.ioCache = null;
    }
}
