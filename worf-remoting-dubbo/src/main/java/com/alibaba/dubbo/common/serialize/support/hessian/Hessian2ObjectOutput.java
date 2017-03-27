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
package com.alibaba.dubbo.common.serialize.support.hessian;

import java.io.IOException;
import java.io.OutputStream;

import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.dubbo.common.serialize.ObjectOutput;

/**
 * Hessian2 Object output.
 *
 * @author qian.lei
 */

public class Hessian2ObjectOutput implements ObjectOutput {
    private final Hessian2Output mH2o;

    public Hessian2ObjectOutput(final OutputStream os) {
        mH2o = new Hessian2Output(os);
        mH2o.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
    }

    @Override
    public void writeBool(final boolean v) throws IOException {
        mH2o.writeBoolean(v);
    }

    @Override
    public void writeByte(final byte v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeShort(final short v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        mH2o.writeLong(v);
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        mH2o.writeDouble(v);
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        mH2o.writeDouble(v);
    }

    @Override
    public void writeBytes(final byte[] b) throws IOException {
        mH2o.writeBytes(b);
    }

    @Override
    public void writeBytes(final byte[] b, final int off, final int len) throws IOException {
        mH2o.writeBytes(b, off, len);
    }

    @Override
    public void writeUTF(final String v) throws IOException {
        mH2o.writeString(v);
    }

    @Override
    public void writeObject(final Object obj) throws IOException {
        mH2o.writeObject(obj);
    }

    @Override
    public void flushBuffer() throws IOException {
        mH2o.flushBuffer();
    }
}