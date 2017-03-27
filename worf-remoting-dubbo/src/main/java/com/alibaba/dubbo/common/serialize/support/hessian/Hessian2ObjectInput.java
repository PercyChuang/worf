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
import java.io.InputStream;
import java.lang.reflect.Type;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.dubbo.common.serialize.ObjectInput;

/**
 * Hessian2 Object input.
 *
 * @author qian.lei
 */

public class Hessian2ObjectInput implements ObjectInput {
    private final Hessian2Input mH2i;

    public Hessian2ObjectInput(final InputStream is) {
        mH2i = new Hessian2Input(is);
        mH2i.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
    }

    @Override
    public boolean readBool() throws IOException {
        return mH2i.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) mH2i.readInt();
    }

    @Override
    public short readShort() throws IOException {
        return (short) mH2i.readInt();
    }

    @Override
    public int readInt() throws IOException {
        return mH2i.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return mH2i.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return (float) mH2i.readDouble();
    }

    @Override
    public double readDouble() throws IOException {
        return mH2i.readDouble();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return mH2i.readBytes();
    }

    @Override
    public String readUTF() throws IOException {
        return mH2i.readString();
    }

    @Override
    public Object readObject() throws IOException {
        return mH2i.readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(final Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) mH2i.readObject(cls);
    }

    @Override
    public <T> T readObject(final Class<T> cls, final Type type) throws IOException, ClassNotFoundException {
        return readObject(cls);
    }

}