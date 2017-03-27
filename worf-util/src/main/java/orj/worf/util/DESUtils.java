package orj.worf.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public final class DESUtils {
    private static final String UTF_8 = "UTF-8";
    private static final String ALGORITHM = "DESede";
    private static final byte[] CRYPT_KEY = { 70, 105, 110, 100, 84, 104, 101, 66, 101, 115, 116, 73, 110, 69, 118,
            101, 114, 121, 98, 111, 100, 121, 42, 46 };
    private static final SecretKey DESKEY;
    private static final SecureRandom RANDOM = new SecureRandom();
    static {
        RANDOM.setSeed(1L);
        DESKEY = new SecretKeySpec(CRYPT_KEY, ALGORITHM);
    }

    private DESUtils() {
    }

    public static String encrypt(final String str) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, DESKEY, RANDOM);
            byte[] encryptStrBytes = cipher.doFinal(str.getBytes(UTF_8));
            return Base64.encodeBase64String(encryptStrBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用DES算法解密.
     *
     * @param str DES加密的密文
     * @return 明文,发生异常时抛出 RuntimeException
     */
    public static String decrypt(final String str) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, DESKEY, RANDOM);
            byte[] decryptStrBytes = cipher.doFinal(Base64.decodeBase64(str));
            return new String(decryptStrBytes, UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
