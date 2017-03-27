package orj.worf.security.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import orj.worf.core.util.ResourceUtils;
import org.apache.commons.codec.binary.Base64;

class RSAUtil4IOS {
    private static final RSAPrivateKey privateKey = loadPrivatekey();
    private static final RSAPublicKey publicKey = loadPublickey();

    private static RSAPrivateKey loadPrivatekey() {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(loadKeyContent("classpath:orj/worf/security/util/data/ios/java_private_key.pem")));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return ((RSAPrivateKey) keyFactory.generatePrivate(keySpec));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RSAPublicKey loadPublickey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                    Base64.decodeBase64(loadKeyContent("classpath:orj/worf/security/util/data/ios/java_public_key.pem")));

            return ((RSAPublicKey) keyFactory.generatePublic(keySpec));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadKeyContent(String path) {
        StringBuilder buf = new StringBuilder(1024);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceUtils.getResourceAsStream(path)));
            Throwable localThrowable2 = null;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.charAt(0) == '-') {
                        continue;
                    }
                    buf.append(line).append('\r');
                }
                String str1 = buf.toString();
                return str1;
            } catch (Throwable localThrowable1) {
            } finally {
                if (reader != null)
                    if (localThrowable2 != null)
                        try {
                            reader.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else
                        reader.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static byte[] encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        return cipher.doFinal(plainText.getBytes());
    }

    public static byte[] decrypt(byte[] cipherData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, privateKey);
        return cipher.doFinal(cipherData);
    }

    public static String decrypt(String cipherText) throws Exception {
        String plainText = new String(decrypt(Base64.decodeBase64(cipherText)));
        if ((plainText != null) && (plainText.endsWith(".IOS.MOBILE"))) {
            return plainText.substring(0, plainText.lastIndexOf(".IOS.MOBILE"));
        }
        throw new RuntimeException("Decryption failed.");
    }
}
