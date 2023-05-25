package com.fluffcord.patcher.cache;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;

final class IOCacheInputStream extends FilterInputStream {
    IOCacheInputStream(byte[] data) {
        super(new ByteArrayInputStream(data));
    }

    @Override
    public int read() throws IOException {
        if (in == null) {
            throw new IOException("Stream Closed");
        }
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (in == null) {
            throw new IOException("Stream Closed");
        }
        return in.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        // Clear reference if closed
        this.in = null;
    }
}
