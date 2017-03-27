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

import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class HeapChannelBufferFactory implements ChannelBufferFactory {

    private static final HeapChannelBufferFactory INSTANCE = new HeapChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    public HeapChannelBufferFactory() {
        super();
    }

    @Override
    public ChannelBuffer getBuffer(final int capacity) {
        return ChannelBuffers.buffer(capacity);
    }

    @Override
    public ChannelBuffer getBuffer(final byte[] array, final int offset, final int length) {
        return ChannelBuffers.wrappedBuffer(array, offset, length);
    }

    @Override
    public ChannelBuffer getBuffer(final ByteBuffer nioBuffer) {
        if (nioBuffer.hasArray()) {
            return ChannelBuffers.wrappedBuffer(nioBuffer);
        }

        ChannelBuffer buf = getBuffer(nioBuffer.remaining());
        int pos = nioBuffer.position();
        buf.writeBytes(nioBuffer);
        nioBuffer.position(pos);
        return buf;
    }

}