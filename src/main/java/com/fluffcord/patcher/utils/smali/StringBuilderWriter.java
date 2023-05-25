package com.fluffcord.patcher.utils.smali;

import java.io.IOException;
import java.io.Writer;

public class StringBuilderWriter extends Writer {
    private final StringBuilder stringBuilder;

    public StringBuilderWriter(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.stringBuilder.append(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
