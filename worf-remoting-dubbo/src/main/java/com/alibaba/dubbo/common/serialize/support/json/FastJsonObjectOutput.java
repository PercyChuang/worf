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
package com.alibaba.dubbo.common.serialize.support.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JsonObjectOutput
 *
 * @author william.liangf
 */
public class FastJsonObjectOutput implements ObjectOutput {

    private final PrintWriter writer;

    public FastJsonObjectOutput(final OutputStream out) {
        this(new OutputStreamWriter(out));
    }

    public FastJsonObjectOutput(final Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    @Override
    public void writeBool(final boolean v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeByte(final byte v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeShort(final short v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeUTF(final String v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeBytes(final byte[] b) throws IOException {
        writer.println(new String(b));
    }

    @Override
    public void writeBytes(final byte[] b, final int off, final int len) throws IOException {
        writer.println(new String(b, off, len));
    }

    @Override
    public void writeObject(final Object obj) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.write(obj);
        out.writeTo(writer);
        writer.println();
        writer.flush();
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
    }

}