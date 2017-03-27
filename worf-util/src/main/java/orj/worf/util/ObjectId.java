/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// ObjectId.java
package orj.worf.util;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A globally unique identifier for objects.
 * <p>Consists of 12 bytes, divided as follows:
 * <blockquote><pre>
 * <table border="1">
 * <tr><td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
 *     <td>7</td><td>8</td><td>9</td><td>10</td><td>11</td></tr>
 * <tr><td colspan="4">time</td><td colspan="3">machine</td>
 *     <td colspan="2">pid</td><td colspan="3">inc</td></tr>
 * </table>
 * </pre></blockquote>
 *
 * @dochub objectids
 */
public final class ObjectId implements Comparable<ObjectId>, java.io.Serializable {
    private static final long serialVersionUID = -8360102104975204190L;
    private static final Logger logger = LoggerFactory.getLogger(ObjectId.class);

    /** Gets a new object id.
     * @return the new id
     */
    public static ObjectId get() {
        return new ObjectId();
    }

    public static String getIdentityId() {
        return DateUtils.format("yyMMdd", new Date()) + getId();
    }

    public static String getId() {
        return get().toHexString();
    }

    public static Date convertToDate(final String objectId) {
        byte b[] = new byte[12];
        String id = objectId;
        if (objectId.length() == 30) {
            id = objectId.substring(6);
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(id.substring(i * 2, i * 2 + 2), 16);
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        return new Date(bb.getInt() * 1000L);
    }

    /** Create a new object id.
     */
    private ObjectId() {
        time = (int) (System.currentTimeMillis() / 1000);
        machine = genmachine;
        inc = nextInc.getAndIncrement();
    }

    @Override
    public int hashCode() {
        int x = time;
        x += machine * 111;
        x += inc * 17;
        return x;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectId)) {
            return false;
        }
        ObjectId other = (ObjectId) o;
        return time == other.time && machine == other.machine && inc == other.inc;
    }

    @Override
    public String toString() {
        return toHexString();
    }

    /**
     * Converts this instance into a 24-byte hexadecimal string representation.
     *
     * @return a string representation of the ObjectId in hexadecimal format
     */
    private String toHexString() {
        final StringBuilder buf = new StringBuilder(24);
        for (final byte b : toByteArray()) {
            buf.append(String.format("%02x", b & 0xff));
        }
        return buf.toString();
    }

    private byte[] toByteArray() {
        byte b[] = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt(time);
        bb.putInt(machine);
        bb.putInt(inc);
        return b;
    }

    @Override
    public int compareTo(final ObjectId id) {
        if (id == null) {
            return -1;
        }
        int x = compareUnsigned(time, id.time);
        if (x != 0) {
            return x;
        }
        x = compareUnsigned(machine, id.machine);
        if (x != 0) {
            return x;
        }
        return compareUnsigned(inc, id.inc);
    }

    private int compareUnsigned(final int i, final int j) {
        long li = 0xFFFFFFFFL;
        li = i & li;
        long lj = 0xFFFFFFFFL;
        lj = j & lj;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;
    }

    private final int time;
    private final int machine;
    private final int inc;

    private static AtomicInteger nextInc = new AtomicInteger(new Random().nextInt());
    private static final int genmachine;
    static {
        try {
            // build a 2-byte machine piece based on NICs info
            int machinePiece;
            {
                try {
                    StringBuilder sb = new StringBuilder(64);
                    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                    while (e.hasMoreElements()) {
                        NetworkInterface ni = e.nextElement();
                        sb.append(ni.toString());
                    }
                    machinePiece = sb.toString().hashCode() << 16;
                } catch (Throwable e) {
                    // exception sometimes happens with IBM JVM, use random
                    logger.warn(e.getMessage(), e);
                    machinePiece = new Random().nextInt() << 16;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("machine piece post: " + Integer.toHexString(machinePiece));
                }
            }
            // add a 2 byte process piece. It must represent not only the JVM but the class loader.
            // Since static var belong to class loader there could be collisions otherwise
            final int processPiece;
            {
                int processId = new Random().nextInt();
                try {
                    processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
                } catch (Throwable t) {
                }
                ClassLoader loader = ObjectId.class.getClassLoader();
                int loaderId = loader != null ? System.identityHashCode(loader) : 0;
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toHexString(processId));
                sb.append(Integer.toHexString(loaderId));
                processPiece = sb.toString().hashCode() & 0xFFFF;
                if (logger.isDebugEnabled()) {
                    logger.debug("process piece: " + Integer.toHexString(processPiece));
                }
            }
            genmachine = machinePiece | processPiece;
            if (logger.isDebugEnabled()) {
                logger.debug("machine : " + Integer.toHexString(genmachine));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}