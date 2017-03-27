/*
 * Copyright 1999-2011 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JSONArray.
 *
 * @author qian.lei
 */

public class JSONArray implements JSONNode {
    private List<Object> mArray = new ArrayList<Object>();

    /**
     * get.
     * 
     * @param index index.
     * @return boolean or long or double or String or JSONArray or JSONObject or null.
     */
    public Object get(final int index) {
        return mArray.get(index);
    }

    /**
     * get boolean value.
     * 
     * @param index index.
     * @param def default value.
     * @return value or default value.
     */
    public boolean getBoolean(final int index, final boolean def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Boolean ? ((Boolean) tmp).booleanValue() : def;
    }

    /**
     * get int value.
     * 
     * @param index index.
     * @param def default value.
     * @return value or default value.
     */
    public int getInt(final int index, final int def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).intValue() : def;
    }

    /**
     * get long value.
     * 
     * @param index index.
     * @param def default value.
     * @return value or default value.
     */
    public long getLong(final int index, final long def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).longValue() : def;
    }

    /**
     * get float value.
     * 
     * @param index index.
     * @param def default value.
     * @return value or default value.
     */
    public float getFloat(final int index, final float def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).floatValue() : def;
    }

    /**
     * get double value.
     * 
     * @param index index.
     * @param def default value.
     * @return value or default value.
     */
    public double getDouble(final int index, final double def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).doubleValue() : def;
    }

    /**
     * get string value.
     * 
     * @param index index.
     * @return value or default value.
     */
    public String getString(final int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp.toString();
    }

    /**
     * get JSONArray value.
     * 
     * @param index index.
     * @return value or default value.
     */
    public JSONArray getArray(final int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp instanceof JSONArray ? (JSONArray) tmp : null;
    }

    /**
     * get JSONObject value.
     * 
     * @param index index.
     * @return value or default value.
     */
    public JSONObject getObject(final int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp instanceof JSONObject ? (JSONObject) tmp : null;
    }

    /**
     * get array length.
     * 
     * @return length.
     */
    public int length() {
        return mArray.size();
    }

    /**
     * add item.
     */
    public void add(final Object ele) {
        mArray.add(ele);
    }

    /**
     * add items.
     */
    public void addAll(final Object[] eles) {
        for (Object ele : eles) {
            mArray.add(ele);
        }
    }

    /**
     * add items.
     */
    public void addAll(final Collection<?> c) {
        mArray.addAll(c);
    }

    /**
     * write json.
     * 
     * @param jc json converter
     * @param jb json builder.
     */
    @Override
    public void writeJSON(final JSONConverter jc, final JSONWriter jb, final boolean writeClass) throws IOException {
        jb.arrayBegin();
        for (Object item : mArray) {
            if (item == null) {
                jb.valueNull();
            } else {
                jc.writeValue(item, jb, writeClass);
            }
        }
        jb.arrayEnd();
    }
}