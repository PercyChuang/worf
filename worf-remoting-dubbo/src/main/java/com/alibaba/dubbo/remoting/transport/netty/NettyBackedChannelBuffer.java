package com.alibaba.dubbo.remoting.transport.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferFactory;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffers;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
public class NettyBackedChannelBuffer implements ChannelBuffer {

    private org.jboss.netty.buffer.ChannelBuffer buffer;

    public org.jboss.netty.buffer.ChannelBuffer nettyChannelBuffer() {
        return buffer;
    }

    public NettyBackedChannelBuffer(final org.jboss.netty.buffer.ChannelBuffer buffer) {
        Assert.notNull(buffer, "buffer == null");
        this.buffer = buffer;
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public ChannelBuffer copy(final int index, final int length) {
        return new NettyBackedChannelBuffer(buffer.copy(index, length));
    }

    @Override
    public ChannelBufferFactory factory() {
        return NettyBackedChannelBufferFactory.getInstance();
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
        // careful
        byte[] data = new byte[length];
        buffer.getBytes(index, data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
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
        // careful
        byte[] data = new byte[length];
        buffer.getBytes(srcIndex, data, 0, length);
        setBytes(0, data, index, length);
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

    // AbstractChannelBuffer

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public ChannelBuffer copy() {
        return new NettyBackedChannelBuffer(buffer.copy());
    }

    @Override
    public void discardReadBytes() {
        buffer.discardReadBytes();
    }

    @Override
    public void ensureWritableBytes(final int writableBytes) {
        buffer.ensureWritableBytes(writableBytes);
    }

    @Override
    public void getBytes(final int index, final byte[] dst) {
        buffer.getBytes(index, dst);
    }

    @Override
    public void getBytes(final int index, final ChannelBuffer dst) {
        // careful
        getBytes(index, dst, dst.writableBytes());
    }

    @Override
    public void getBytes(final int index, final ChannelBuffer dst, final int length) {
        // careful
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }

    @Override
    public void markReaderIndex() {
        buffer.markReaderIndex();
    }

    @Override
    public void markWriterIndex() {
        buffer.markWriterIndex();
    }

    @Override
    public boolean readable() {
        return buffer.readable();
    }

    @Override
    public int readableBytes() {
        return buffer.readableBytes();
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public void readBytes(final byte[] dst) {
        buffer.readBytes(dst);
    }

    @Override
    public void readBytes(final byte[] dst, final int dstIndex, final int length) {
        buffer.readBytes(dst, dstIndex, length);
    }

    @Override
    public void readBytes(final ByteBuffer dst) {
        buffer.readBytes(dst);
    }

    @Override
    public void readBytes(final ChannelBuffer dst) {
        // careful
        readBytes(dst, dst.writableBytes());
    }

    @Override
    public void readBytes(final ChannelBuffer dst, final int length) {
        // carefule
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
    }

    @Override
    public void readBytes(final ChannelBuffer dst, final int dstIndex, final int length) {
        // careful
        if (readableBytes() < length) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = new byte[length];
        buffer.readBytes(data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }

    @Override
    public ChannelBuffer readBytes(final int length) {
        return new NettyBackedChannelBuffer(buffer.readBytes(length));
    }

    @Override
    public void resetReaderIndex() {
        buffer.resetReaderIndex();
    }

    @Override
    public void resetWriterIndex() {
        buffer.resetWriterIndex();
    }

    @Override
    public int readerIndex() {
        return buffer.readerIndex();
    }

    @Override
    public void readerIndex(final int readerIndex) {
        buffer.readerIndex(readerIndex);
    }

    @Override
    public void readBytes(final OutputStream dst, final int length) throws IOException {
        buffer.readBytes(dst, length);
    }

    @Override
    public void setBytes(final int index, final byte[] src) {
        buffer.setBytes(index, src);
    }

    @Override
    public void setBytes(final int index, final ChannelBuffer src) {
        // careful
        setBytes(index, src, src.readableBytes());
    }

    @Override
    public void setBytes(final int index, final ChannelBuffer src, final int length) {
        // careful
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
    }

    @Override
    public void setIndex(final int readerIndex, final int writerIndex) {
        buffer.setIndex(readerIndex, writerIndex);
    }

    @Override
    public void skipBytes(final int length) {
        buffer.skipBytes(length);
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return buffer.toByteBuffer();
    }

    @Override
    public boolean writable() {
        return buffer.writable();
    }

    @Override
    public int writableBytes() {
        return buffer.writableBytes();
    }

    @Override
    public void writeByte(final int value) {
        buffer.writeByte(value);
    }

    @Override
    public void writeBytes(final byte[] src) {
        buffer.writeBytes(src);
    }

    @Override
    public void writeBytes(final byte[] src, final int index, final int length) {
        buffer.writeBytes(src, index, length);
    }

    @Override
    public void writeBytes(final ByteBuffer src) {
        buffer.writeBytes(src);
    }

    @Override
    public void writeBytes(final ChannelBuffer src) {
        // careful
        writeBytes(src, src.readableBytes());
    }

    @Override
    public void writeBytes(final ChannelBuffer src, final int length) {
        // careful
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        }
        writeBytes(src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
    }

    @Override
    public void writeBytes(final ChannelBuffer src, final int srcIndex, final int length) {
        // careful
        byte[] data = new byte[length];
        src.getBytes(srcIndex, data, 0, length);
        writeBytes(data, 0, length);
    }

    @Override
    public int writeBytes(final InputStream src, final int length) throws IOException {
        return buffer.writeBytes(src, length);
    }

    @Override
    public int writerIndex() {
        return buffer.writerIndex();
    }

    @Override
    public void writerIndex(final int writerIndex) {
        buffer.writerIndex(writerIndex);
    }

    @Override
    public int compareTo(final ChannelBuffer o) {
        return ChannelBuffers.compare(this, o);
    }
}
