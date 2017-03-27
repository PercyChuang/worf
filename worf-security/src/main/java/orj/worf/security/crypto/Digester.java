package orj.worf.security.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import orj.worf.util.Hex;

public final class Digester {
    private final MessageDigest messageDigest;
    private final int iterations;

    public Digester(String algorithm, int iterations) {
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No such hashing algorithm", e);
        }
        this.iterations = ((iterations < 1) ? 1 : iterations);
    }

    public byte[] digest(byte[] value) {
        synchronized (this.messageDigest) {
            for (int i = 0; i < this.iterations; ++i) {
                value = this.messageDigest.digest(value);
                this.messageDigest.reset();
            }
            return value;
        }
    }

    public String digestHex(String value) {
        return Hex.encodeHexString(digest(value.getBytes()));
    }
}
