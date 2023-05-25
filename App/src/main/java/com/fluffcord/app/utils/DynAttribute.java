package com.fluffcord.app.utils;

import java.util.Map;

public class DynAttribute implements Map.Entry<String, String> {
    private final String key;
    private String value;

    public DynAttribute(String key) {
        this(key, null);
    }

    public DynAttribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }
}
