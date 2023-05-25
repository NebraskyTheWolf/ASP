package com.fluffcord.app.utils;

import android.util.AttributeSet;

import com.fluffcord.app.FluffCord;

import java.util.ArrayList;
import java.util.HashMap;

public class DynAttributeSet implements AttributeSet {
    private final ArrayList<DynAttribute> attrs = new ArrayList<>(16);
    private final HashMap<String, DynAttribute> attrMap = new HashMap<>();

    public DynAttribute getAttribute(int index) {
        return attrs.get(index);
    }

    public DynAttribute getAttribute(String name) {
        return attrMap.get(name);
    }

    @Override
    public int getAttributeCount() {
        return attrs.size();
    }

    @Override
    public String getAttributeName(int index) {
        return this.getAttribute(index).getKey();
    }

    @Override
    public String getAttributeValue(int index) {
        return this.getAttribute(index).getValue();
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        DynAttribute dynAttribute = this.getAttribute(namespace+":"+name);
        if (dynAttribute == null) {
            dynAttribute = this.getAttribute(name);
        }
        return dynAttribute == null ? null : dynAttribute.getValue();
    }

    @Override
    public String getPositionDescription() {
        return null;
    }

    @Override
    public int getAttributeNameResource(int index) {
        return FluffCord.getResourceId(this.getAttribute(index).getValue());
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute,
                                     String[] options, int defaultValue) {
        DynAttribute dynAttribute = this.getAttribute(namespace+":"+attribute);
        if (dynAttribute == null) {
            dynAttribute = this.getAttribute(attribute);
        }
        return dynAttribute == null ? defaultValue :
                FluffCord.getResourceId(dynAttribute.getValue(), defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        return false;
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        return 0;
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        return 0;
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        return 0;
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        return 0;
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return 0;
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return false;
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue) {
        return 0;
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        return 0;
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return 0;
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        return 0;
    }

    @Override
    public String getIdAttribute() {
        return null;
    }

    @Override
    public String getClassAttribute() {
        return null;
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        return defaultValue;
    }

    @Override
    public int getStyleAttribute() {
        return 0;
    }
}
