/*
 * Copyright 1999-2012 Alibaba Group.
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

package com.alibaba.dubbo.remoting.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class DynamicChannelBuffer extends AbstractChannelBuffer {

    private final ChannelBufferFactory factory;

    private ChannelBuffer buffer;

    public DynamicChannelBuffer(final int estimatedLength) {
        this(estimatedLength, HeapChannelBufferFactory.getInstance());
    }

    public DynamicChannelBuffer(final int estimatedLength, final ChannelBufferFactory factory) {
        if (estimatedLength < 0) {
            throw new IllegalArgumentException("estimatedLength: " + estimatedLength);
        }
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        this.factory = factory;
        buffer = factory.getBuffer(estimatedLength);
    }

    @Override
    public void ensureWritableBytes(final int minWritableBytes) {
        if (minWritableBytes <= writableBytes()) {
            return;
        }

        int newCapacity;
        if (capacity() == 0) {
            newCapacity = 1;
        } else {
            newCapacity = capacity();
        }
        int minNewCapacity = writerIndex() + minWritableBytes;
        while (newCapacity < minNewCapacity) {
            newCapacity <<= 1;
        }

        ChannelBuffer newBuffer = factory().getBuffer(newCapacity);
        newBuffer.writeBytes(buffer, 0, writerIndex());
        buffer = newBuffer;
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public ChannelBuffer copy(final int index, final int length) {
        DynamicChannelBuffer copiedBuffer = new DynamicChannelBuffer(Math.max(length, 64), factory());
        copiedBuffer.buffer = buffer.copy(index, length);
        copiedBuffer.setIndex(0, length);
        return copiedBuffer;
    }

    @Override
    public ChannelBufferFactory factory() {
        return factory;
    }

    @Override
    public byte getByte(final int index) {
        return buffer.getByte(index);
    }

    @Override
    public void getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        buffer.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public void getBytes(final int index, final ByteBuffer dst) {
        buffer.getBytes(index, dst);
    }

    @Override
    public void getBytes(final int index, final ChannelBuffer dst, final int dstIndex, final int length) {
        buffer.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public void getBytes(final int index, final OutputStream dst, final int length) throws IOException {
        buffer.getBytes(index, dst, length);
    }

    @Override
    public boolean isDirect() {
        return buffer.isDirect();
    }

    @Override
    public void setByte(final int index, final int value) {
        buffer.setByte(index, value);
    }

    @Override
    public void setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        buffer.setBytes(index, src, srcIndex, length);
    }

    @Override
    public void setBytes(final int index, final ByteBuffer src) {
        buffer.setBytes(index, src);
    }

    @Override
    public void setBytes(final int index, final ChannelBuffer src, final int srcIndex, final int length) {
        buffer.setBytes(index, src, srcIndex, length);
    }

    @Override
    public int setBytes(final int index, final InputStream src, final int length) throws IOException {
        return buffer.setBytes(index, src, length);
    }

    @Override
    public ByteBuffer toByteBuffer(final int index, final int length) {
        return buffer.toByteBuffer(index, length);
    }

    @Override
    public void writeByte(final int value) {
        ensureWritableBytes(1);
        super.writeByte(value);
    }

    @Override
    public void writeBytes(final byte[] src, final int srcIndex, final int length) {
        ensureWritableBytes(length);
        super.writeBytes(src, srcIndex, length);
    }

    @Override
    public void writeBytes(final ChannelBuffer src, final int srcIndex, final int length) {
        ensureWritableBytes(length);
        super.writeBytes(src, srcIndex, length);
    }

    @Override
    public void writeBytes(final ByteBuffer src) {
        ensureWritableBytes(src.remaining());
        super.writeBytes(src);
    }

    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        ensureWritableBytes(length);
        return super.writeBytes(in, length);
    }

    @Override
    public byte[] array() {
        return buffer.array();
    }

    @Override
    public boolean hasArray() {
        return buffer.hasArray();
    }

    @Override
    public int arrayOffset() {
        return buffer.arrayOffset();
    }
}
