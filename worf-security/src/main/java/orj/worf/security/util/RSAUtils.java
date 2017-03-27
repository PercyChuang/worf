package orj.worf.security.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import orj.worf.util.DataCache;
import orj.worf.util.Hex;
import orj.worf.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

final class RSAUtils {
    private static final KeyFactory keyFactory;
    private static final KeyPair keyPair_1024;
    private static DataCache<String, String> cache;

    static void launch() {
        RSAUtils.class.hashCode();
    }

    static String getModulus() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair_1024.getPublic();
        return new String(Hex.encode(publicKey.getModulus().toByteArray()));
    }

    static String getPublicExponent() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair_1024.getPublic();
        return new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    }

    static String encryptByPublicKey(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(1, keyPair_1024.getPublic());
        return Hex.encodeHexString(cipher.doFinal(data.getBytes()));
    }

    static String decryptByPrivateKey(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(2, keyPair_1024.getPrivate());
        return new String(cipher.doFinal(Hex.decode(data)));
    }

    private static KeyPair getKeyPair() {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    ResourceUtils.getResourceAsStream("classpath:orj/worf/security/util/data/RSA1024.dat"));
            Throwable localThrowable2 = null;
            try {
                KeyPair localKeyPair = (KeyPair) ois.readObject();
                return localKeyPair;
            } catch (Throwable localThrowable3) {
            } finally {
                if (ois != null)
                    if (localThrowable2 != null)
                        try {
                            ois.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else
                        ois.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    static String getKeyString(Key key) {
        byte[] keyBytes = key.getEncoded();
        return Hex.encodeHexString(keyBytes);
    }

    static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Hex.decode(key));
        return keyFactory.generatePublic(keySpec);
    }

    static PrivateKey getPrivateKey(String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Hex.decode(key));
        return keyFactory.generatePrivate(keySpec);
    }

    static String decryptFromRSAJS(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(2, keyPair_1024.getPrivate());
        String text = new String(cipher.doFinal(Hex.decode(data)));
        if (StringUtils.isNotBlank(text)) {
            String txt = StringUtils.reverse(text);
            return txt.substring(7);
        }
        throw new RuntimeException("Decryption failed.");
    }

    static String decryptFromMobileRSA(String cipherText) throws Exception {
        String plainText = (String) cache.get(cipherText);
        if (plainText != null)
            return plainText;
        try {
            plainText = decryptForAndroidApp(cipherText);
        } catch (Exception e) {
            plainText = RSAUtil4IOS.decrypt(cipherText);
        }
        cache.put(cipherText, plainText);
        return plainText;
    }

    private static String decryptForAndroidApp(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(2, keyPair_1024.getPrivate());
        String plainText = new String(cipher.doFinal(Hex.decode(data)));
        if ((plainText != null) && (plainText.endsWith(".RSA.MOBILE"))) {
            return plainText.substring(0, plainText.lastIndexOf(".RSA.MOBILE"));
        }
        throw new RuntimeException("Decryption failed.");
    }

    static String transferToJSRSA(String cipherText) throws Exception {
        String plainText = decryptFromMobileRSA(cipherText);
        plainText = new StringBuilder("MOBILE." + plainText).reverse().toString();
        return encryptByPublicKey(plainText);
    }

    private static void generateKeyPair() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("c:/RSAkeyPair.dat")));
            Throwable localThrowable2 = null;
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");

                keyPairGenerator.initialize(1024);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                oos.writeObject(keyPair);
                oos.flush();
            } catch (Throwable localThrowable1) {
            } finally {
                if (oos != null)
                    if (localThrowable2 != null)
                        try {
                            oos.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else
                        oos.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyFactory = KeyFactory.getInstance("RSA", "BC");
            keyPair_1024 = getKeyPair();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        cache = new DataCache("RSA-APP", 86400000L);
    }
}