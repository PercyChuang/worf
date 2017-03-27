package orj.worf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.ByteArrayBuffer;

public class Bytes {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;
    private static final char[] BASE16 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };
    private static final char[] BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
    private static final Map<Integer, byte[]> DECODE_TABLE_MAP = new ConcurrentHashMap<Integer, byte[]>();
    private static ThreadLocal<MessageDigest> MD = new ThreadLocal<MessageDigest>();

    public static byte[] toBytes(final InputStream instream, final int length) throws IOException {
        org.springframework.util.Assert.notNull(instream, "instream参数不能为null");
        ByteArrayBuffer buffer = new ByteArrayBuffer(length);
        byte[] tmp = new byte[DEFAULT_BUFFER_SIZE];
        int l;
        while ((l = instream.read(tmp)) != -1) {
            buffer.append(tmp, 0, l);
        }
        return buffer.toByteArray();
    }

    /**
     * 转换指定长度的byte数组为字符串
     */
    public static String toString(final byte[] src, final int beginIndex, final int endIndex) {
        if (src == null) {
            return "null";
        }
        int iMax = src.length - 1;
        if (iMax == -1) {
            return "[]";
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(src[i]);
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(", ");
        }
    }

    public static byte[] copyOf(final byte[] src, final int length) {
        byte[] dest = new byte[length];
        System.arraycopy(src, 0, dest, 0, Math.min(src.length, length));
        return dest;
    }

    public static byte[] short2bytes(final short v) {
        byte[] ret = new byte[2];
        short2bytes(v, ret);
        return ret;
    }

    public static void short2bytes(final short v, final byte[] b) {
        short2bytes(v, b, 0);
    }

    public static void short2bytes(final short v, final byte[] b, final int off) {
        b[off + 1] = (byte) v;
        b[off + 0] = (byte) (v >>> 8);
    }

    public static byte[] int2bytes(final int v) {
        byte[] ret = new byte[4];
        int2bytes(v, ret);
        return ret;
    }

    public static void int2bytes(final int v, final byte[] b) {
        int2bytes(v, b, 0);
    }

    public static void int2bytes(final int v, final byte[] b, final int off) {
        b[off + 3] = (byte) v;
        b[off + 2] = (byte) (v >>> 8);
        b[off + 1] = (byte) (v >>> 16);
        b[off + 0] = (byte) (v >>> 24);
    }

    public static byte[] float2bytes(final float v) {
        byte[] ret = new byte[4];
        float2bytes(v, ret);
        return ret;
    }

    public static void float2bytes(final float v, final byte[] b) {
        float2bytes(v, b, 0);
    }

    public static void float2bytes(final float v, final byte[] b, final int off) {
        int i = Float.floatToIntBits(v);
        b[off + 3] = (byte) i;
        b[off + 2] = (byte) (i >>> 8);
        b[off + 1] = (byte) (i >>> 16);
        b[off + 0] = (byte) (i >>> 24);
    }

    public static byte[] long2bytes(final long v) {
        byte[] ret = new byte[8];
        long2bytes(v, ret);
        return ret;
    }

    public static void long2bytes(final long v, final byte[] b) {
        long2bytes(v, b, 0);
    }

    public static void long2bytes(final long v, final byte[] b, final int off) {
        b[off + 7] = (byte) (int) v;
        b[off + 6] = (byte) (int) (v >>> 8);
        b[off + 5] = (byte) (int) (v >>> 16);
        b[off + 4] = (byte) (int) (v >>> 24);
        b[off + 3] = (byte) (int) (v >>> 32);
        b[off + 2] = (byte) (int) (v >>> 40);
        b[off + 1] = (byte) (int) (v >>> 48);
        b[off + 0] = (byte) (int) (v >>> 56);
    }

    public static byte[] double2bytes(final double v) {
        byte[] ret = new byte[8];
        double2bytes(v, ret);
        return ret;
    }

    public static void double2bytes(final double v, final byte[] b) {
        double2bytes(v, b, 0);
    }

    public static void double2bytes(final double v, final byte[] b, final int off) {
        long j = Double.doubleToLongBits(v);
        b[off + 7] = (byte) (int) j;
        b[off + 6] = (byte) (int) (j >>> 8);
        b[off + 5] = (byte) (int) (j >>> 16);
        b[off + 4] = (byte) (int) (j >>> 24);
        b[off + 3] = (byte) (int) (j >>> 32);
        b[off + 2] = (byte) (int) (j >>> 40);
        b[off + 1] = (byte) (int) (j >>> 48);
        b[off + 0] = (byte) (int) (j >>> 56);
    }

    public static short bytes2short(final byte[] b) {
        return bytes2short(b, 0);
    }

    public static short bytes2short(final byte[] b, final int off) {
        return (short) (((b[off + 1] & 0xFF) << 0) + (b[off + 0] << 8));
    }

    public static int bytes2int(final byte[] b) {
        return bytes2int(b, 0);
    }

    public static int bytes2int(final byte[] b, final int off) {
        return ((b[off + 3] & 0xFF) << 0) + ((b[off + 2] & 0xFF) << 8) + ((b[off + 1] & 0xFF) << 16)
                + (b[off + 0] << 24);
    }

    public static float bytes2float(final byte[] b) {
        return bytes2float(b, 0);
    }

    public static float bytes2float(final byte[] b, final int off) {
        int i = ((b[off + 3] & 0xFF) << 0) + ((b[off + 2] & 0xFF) << 8) + ((b[off + 1] & 0xFF) << 16)
                + (b[off + 0] << 24);
        return Float.intBitsToFloat(i);
    }

    public static long bytes2long(final byte[] b) {
        return bytes2long(b, 0);
    }

    public static long bytes2long(final byte[] b, final int off) {
        return ((b[off + 7] & 0xFF) << 0) + ((b[off + 6] & 0xFF) << 8) + ((b[off + 5] & 0xFF) << 16)
                + ((b[off + 4] & 0xFF) << 24) + ((b[off + 3] & 0xFF) << 32) + ((b[off + 2] & 0xFF) << 40)
                + ((b[off + 1] & 0xFF) << 48) + (b[off + 0] << 56);
    }

    public static double bytes2double(final byte[] b) {
        return bytes2double(b, 0);
    }

    public static double bytes2double(final byte[] b, final int off) {
        long j = ((b[off + 7] & 0xFF) << 0) + ((b[off + 6] & 0xFF) << 8) + ((b[off + 5] & 0xFF) << 16)
                + ((b[off + 4] & 0xFF) << 24) + ((b[off + 3] & 0xFF) << 32) + ((b[off + 2] & 0xFF) << 40)
                + ((b[off + 1] & 0xFF) << 48) + (b[off + 0] << 56);
        return Double.longBitsToDouble(j);
    }

    public static String bytes2hex(final byte[] bs) {
        return bytes2hex(bs, 0, bs.length);
    }

    public static String bytes2hex(final byte[] bs, final int off, final int len) {
        if (off < 0) {
            throw new IndexOutOfBoundsException("bytes2hex: offset < 0, offset is " + off);
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("bytes2hex: length < 0, length is " + len);
        }
        if (off + len > bs.length) {
            throw new IndexOutOfBoundsException("bytes2hex: offset + length > array length.");
        }

        int r = off;
        int w = 0;
        char[] cs = new char[len * 2];
        for (int i = 0; i < len; i++) {
            byte b = bs[r++];
            cs[w++] = BASE16[b >> 4 & 0xF];
            cs[w++] = BASE16[b & 0xF];
        }
        return new String(cs);
    }

    public static byte[] hex2bytes(final String str) {
        return hex2bytes(str, 0, str.length());
    }

    public static byte[] hex2bytes(final String str, final int off, final int len) {
        if ((len & 0x1) == 1) {
            throw new IllegalArgumentException("hex2bytes: ( len & 1 ) == 1.");
        }
        if (off < 0) {
            throw new IndexOutOfBoundsException("hex2bytes: offset < 0, offset is " + off);
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("hex2bytes: length < 0, length is " + len);
        }
        if (off + len > str.length()) {
            throw new IndexOutOfBoundsException("hex2bytes: offset + length > array length.");
        }
        int num = len / 2;
        int r = off;
        int w = 0;
        byte[] b = new byte[num];
        for (int i = 0; i < num; i++) {
            b[w++] = (byte) (hex(str.charAt(r++)) << 4 | hex(str.charAt(r++)));
        }
        return b;
    }

    public static String bytes2base64(final byte[] b) {
        return bytes2base64(b, 0, b.length, BASE64);
    }

    public static String bytes2base64(final byte[] b, final int offset, final int length) {
        return bytes2base64(b, offset, length, BASE64);
    }

    public static String bytes2base64(final byte[] b, final String code) {
        return bytes2base64(b, 0, b.length, code);
    }

    public static String bytes2base64(final byte[] b, final int offset, final int length, final String code) {
        if (code.length() < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        }
        return bytes2base64(b, offset, length, code.toCharArray());
    }

    public static String bytes2base64(final byte[] b, final char[] code) {
        return bytes2base64(b, 0, b.length, code);
    }

    public static String bytes2base64(final byte[] bs, final int off, final int len, final char[] code) {
        if (off < 0) {
            throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off);
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len);
        }
        if (off + len > bs.length) {
            throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");
        }
        if (code.length < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        }
        boolean pad = code.length > 64;
        int num = len / 3;
        int rem = len % 3;
        int r = off;
        int w = 0;
        char[] cs = new char[num * 4 + (pad ? 4 : rem == 0 ? 0 : rem + 1)];

        for (int i = 0; i < num; i++) {
            int b1 = bs[r++] & 0xFF;
            int b2 = bs[r++] & 0xFF;
            int b3 = bs[r++] & 0xFF;

            cs[w++] = code[b1 >> 2];
            cs[w++] = code[b1 << 4 & 0x3F | b2 >> 4];
            cs[w++] = code[b2 << 2 & 0x3F | b3 >> 6];
            cs[w++] = code[b3 & 0x3F];
        }

        if (rem == 1) {
            int b1 = bs[r++] & 0xFF;
            cs[w++] = code[b1 >> 2];
            cs[w++] = code[b1 << 4 & 0x3F];
            if (pad) {
                cs[w++] = code[64];
                cs[w++] = code[64];
            }
        } else if (rem == 2) {
            int b1 = bs[r++] & 0xFF;
            int b2 = bs[r++] & 0xFF;
            cs[w++] = code[b1 >> 2];
            cs[w++] = code[b1 << 4 & 0x3F | b2 >> 4];
            cs[w++] = code[b2 << 2 & 0x3F];
            if (pad) {
                cs[w++] = code[64];
            }
        }
        return new String(cs);
    }

    public static byte[] base642bytes(final String str) {
        return base642bytes(str, 0, str.length());
    }

    public static byte[] base642bytes(final String str, final int offset, final int length) {
        return base642bytes(str, offset, length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
    }

    public static byte[] base642bytes(final String str, final String code) {
        return base642bytes(str, 0, str.length(), code);
    }

    public static byte[] base642bytes(final String str, final int off, final int len, final String code) {
        if (off < 0) {
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
        }
        if (off + len > str.length()) {
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
        }
        if (code.length() < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        }
        int rem = len % 4;
        if (rem == 1) {
            throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
        }
        int num = len / 4;
        int size = num * 3;
        if (code.length() > 64) {
            if (rem != 0) {
                throw new IllegalArgumentException("base642bytes: base64 string length error.");
            }
            char pc = code.charAt(64);
            if (str.charAt(off + len - 2) == pc) {
                size -= 2;
                num--;
                rem = 2;
            } else if (str.charAt(off + len - 1) == pc) {
                size--;
                num--;
                rem = 3;
            }
        } else if (rem == 2) {
            size++;
        } else if (rem == 3) {
            size += 2;
        }

        int r = off;
        int w = 0;
        byte[] b = new byte[size];
        byte[] t = decodeTable(code);
        for (int i = 0; i < num; i++) {
            int c1 = t[str.charAt(r++)];
            int c2 = t[str.charAt(r++)];
            int c3 = t[str.charAt(r++)];
            int c4 = t[str.charAt(r++)];

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
            b[w++] = (byte) (c2 << 4 | c3 >> 2);
            b[w++] = (byte) (c3 << 6 | c4);
        }

        if (rem == 2) {
            int c1 = t[str.charAt(r++)];
            int c2 = t[str.charAt(r++)];

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
        } else if (rem == 3) {
            int c1 = t[str.charAt(r++)];
            int c2 = t[str.charAt(r++)];
            int c3 = t[str.charAt(r++)];

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
            b[w++] = (byte) (c2 << 4 | c3 >> 2);
        }
        return b;
    }

    public static byte[] base642bytes(final String str, final char[] code) {
        return base642bytes(str, 0, str.length(), code);
    }

    public static byte[] base642bytes(final String str, final int off, final int len, final char[] code) {
        if (off < 0) {
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
        }
        if (off + len > str.length()) {
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
        }
        if (code.length < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        }
        int rem = len % 4;
        if (rem == 1) {
            throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
        }
        int num = len / 4;
        int size = num * 3;
        if (code.length > 64) {
            if (rem != 0) {
                throw new IllegalArgumentException("base642bytes: base64 string length error.");
            }
            char pc = code[64];
            if (str.charAt(off + len - 2) == pc) {
                size -= 2;
            } else if (str.charAt(off + len - 1) == pc) {
                size--;
            }
        } else if (rem == 2) {
            size++;
        } else if (rem == 3) {
            size += 2;
        }

        int r = off;
        int w = 0;
        byte[] b = new byte[size];
        for (int i = 0; i < num; i++) {
            int c1 = indexOf(code, str.charAt(r++));
            int c2 = indexOf(code, str.charAt(r++));
            int c3 = indexOf(code, str.charAt(r++));
            int c4 = indexOf(code, str.charAt(r++));

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
            b[w++] = (byte) (c2 << 4 | c3 >> 2);
            b[w++] = (byte) (c3 << 6 | c4);
        }

        if (rem == 2) {
            int c1 = indexOf(code, str.charAt(r++));
            int c2 = indexOf(code, str.charAt(r++));

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
        } else if (rem == 3) {
            int c1 = indexOf(code, str.charAt(r++));
            int c2 = indexOf(code, str.charAt(r++));
            int c3 = indexOf(code, str.charAt(r++));

            b[w++] = (byte) (c1 << 2 | c2 >> 4);
            b[w++] = (byte) (c2 << 4 | c3 >> 2);
        }
        return b;
    }

    public static byte[] zip(final byte[] bytes) throws IOException {
        UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
        OutputStream os = new DeflaterOutputStream(bos);
        try {
            os.write(bytes);
        } finally {
            os.close();
            bos.close();
        }
        return bos.toByteArray();
    }

    public static byte[] unzip(final byte[] bytes) throws IOException {
        UnsafeByteArrayInputStream bis = new UnsafeByteArrayInputStream(bytes);
        UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
        InputStream is = new InflaterInputStream(bis);
        try {
            IOUtils.copyLarge(is, bos);
            return bos.toByteArray();
        } finally {
            is.close();
            bis.close();
            bos.close();
        }
    }

    public static byte[] getMD5(final String str) {
        return getMD5(str.getBytes());
    }

    public static byte[] getMD5(final byte[] source) {
        MessageDigest md = getMessageDigest();
        return md.digest(source);
    }

    public static byte[] getMD5(final File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            return getMD5(is);
        } finally {
            is.close();
        }
    }

    public static byte[] getMD5(final InputStream is) throws IOException {
        return getMD5(is, 8192);
    }

    private static byte hex(final char c) {
        if (c <= '9') {
            return (byte) (c - '0');
        }
        if (c >= 'a' && c <= 'f') {
            return (byte) (c - 'a' + 10);
        }
        if (c >= 'A' && c <= 'F') {
            return (byte) (c - 'A' + 10);
        }
        throw new IllegalArgumentException("hex string format error [" + c + "].");
    }

    private static int indexOf(final char[] cs, final char c) {
        int i = 0;
        for (int len = cs.length; i < len; i++) {
            if (cs[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static byte[] decodeTable(final String code) {
        int hash = code.hashCode();
        byte[] ret = DECODE_TABLE_MAP.get(Integer.valueOf(hash));
        if (ret == null) {
            if (code.length() < 64) {
                throw new IllegalArgumentException("Base64 code length < 64.");
            }
            ret = new byte[''];
            for (int i = 0; i < 128; i++) {
                ret[i] = -1;
            }
            for (int i = 0; i < 64; i++) {
                ret[code.charAt(i)] = (byte) i;
            }
            DECODE_TABLE_MAP.put(Integer.valueOf(hash), ret);
        }
        return ret;
    }

    private static byte[] getMD5(final InputStream is, final int bs) throws IOException {
        MessageDigest md = getMessageDigest();
        byte[] buf = new byte[bs];
        while (is.available() > 0) {
            int total = 0;
            do {
                int read;
                if ((read = is.read(buf, total, bs - total)) <= 0) {
                    break;
                }
                total += read;
            } while (total < bs);
            md.update(buf);
        }
        return md.digest();
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest ret = MD.get();
        if (ret == null) {
            try {
                ret = MessageDigest.getInstance("MD5");
                MD.set(ret);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    public static int readInputStreamToArray(final InputStream in, final byte[] buffer, final int length)
            throws IOException {
        int l = -1;
        int actualReadLength = 0;

        while ((l = in.read(buffer, actualReadLength, length - actualReadLength)) != -1) {
            actualReadLength += l;
            if (actualReadLength == length) {
                break;
            }
        }
        return actualReadLength;
    }
}