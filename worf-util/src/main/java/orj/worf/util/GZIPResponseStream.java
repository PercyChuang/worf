package orj.worf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

public class GZIPResponseStream extends ServletOutputStream {
    private ServletOutputStream servletStream;
    private ByteArrayOutputStream byteStream;
    private GZIPOutputStream gzipStream;
    private HttpServletResponse response;
    private boolean closed;

    public GZIPResponseStream(final HttpServletResponse response) throws IOException {
        this.closed = false;
        this.response = response;
        this.servletStream = response.getOutputStream();
        this.byteStream = new ByteArrayOutputStream();
        this.gzipStream = new GZIPOutputStream(this.byteStream);
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            throw new IOException("This output stream has already been closed");
        }
        this.gzipStream.finish();
        byte[] bytes = this.byteStream.toByteArray();
        this.response.setContentLength(bytes.length);
        this.response.addHeader("Content-Encoding", "gzip");
        this.servletStream.write(bytes);
        this.servletStream.flush();
        this.servletStream.close();
        this.closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        this.gzipStream.flush();
    }

    @Override
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        this.gzipStream.write((byte) b);
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        this.gzipStream.write(b, off, len);
    }

    public boolean closed() {
        return this.closed;
    }

    public void reset() {
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWriteListener(WriteListener arg0) {
        // TODO Auto-generated method stub

    }
}