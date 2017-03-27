package orj.worf.util;

import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public final class AESUtils {
    private static final String UTF_8 = "UTF-8";
    private static final String AES = "AES";
    private static final String AES_CBC = "AES/CBC/PKCS7Padding";
    private static final byte[] CRYPT_KEY = { 0x4c, 0x6f, 0x76, 0x65, 0x20, 0x69, 0x73, 0x20, 0x46, 0x6f, 0x72, 0x65,
        0x76, 0x65, 0x72, 0x2e };
    private static final byte[] IV = { 0x59, 0x6f, 0x75, 0x20, 0x61, 0x72, 0x65, 0x20, 0x74, 0x68, 0x65, 0x20, 0x62,
        0x65, 0x73, 0x74 };
    private static final SecretKey AESKEY;
    private static final SecureRandom RANDOM = new SecureRandom();
    static {
        RANDOM.setSeed(1L);
        Security.addProvider(new BouncyCastleProvider());
        AESKEY = new SecretKeySpec(CRYPT_KEY, AES);
    }

    private AESUtils() {
    }

    public static String encrypt(final String str) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, AESKEY, new IvParameterSpec(IV), RANDOM);
            byte[] enc = cipher.doFinal(str.getBytes(UTF_8));
            return Base64.encodeBase64String(enc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用AES算法解密.
     *
     * @param str AES加密的密文
     * @return 明文,发生异常时抛出 RuntimeException
     */
    public static String decrypt(final String str) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC, "BC");
            cipher.init(Cipher.DECRYPT_MODE, AESKEY, new IvParameterSpec(IV), RANDOM);
            byte[] decryptStrBytes = cipher.doFinal(Base64.decodeBase64(str));
            return new String(decryptStrBytes, UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
