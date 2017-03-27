package orj.worf.security.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;

final class SecurityUtils {
    private static final String DEFAULT_SALT = "BRAVA";
    private static final byte[] SK;
    private static final byte[] IV;

    static void launch() {
        SecurityUtils.class.hashCode();
    }

    public static String encodePassword(String password, String salt) throws Exception {
        if (password == null) {
            throw new IllegalArgumentException();
        }
        String saltStr = (salt == null) ? "BRAVA" : salt;
        byte[] key = SK;
        byte[] info = EncryptionTools.encodeStrongPassword(packData(password + saltStr), key);
        return DigestUtils.md5Hex(info);
    }

    public static String encryptByAES(String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        byte[] key = SK;
        byte[] ivs = IV;
        byte[] info = EncryptionTools.encryptSensitiveInformation(packData(data), key, ivs);
        return Hex.toHexString(info);
    }

    public static String decryptByAES(String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        byte[] key = SK;
        byte[] ivs = IV;
        return verifyAndReturn(EncryptionTools.decryptSensitiveInformationToString(Hex.decode(data), key, ivs));
    }

    public static String encryptByPublicKey(String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = EncryptionTools.encryptByPublicKey(orj.worf.util.StringUtils.getBytesUtf8(packData(data)),
                "classpath:orj/worf/security/util/data/Sensitive.pub");

        return Hex.toHexString(bytes);
    }

    public static String decryptByPrivateKey(String data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = Hex.decode(data);
        byte[] info = EncryptionTools.decryptByPrivateKey(bytes, "classpath:orj/worf/security/util/data/Security.sec",
                Constants.KSP, "DefaultKey", Constants.DKP);

        return verifyAndReturn(orj.worf.util.StringUtils.newStringUtf8(info));
    }

    private static final String packData(String data) {
        return "{D}" + data + "{G}";
    }

    private static final String verifyAndReturn(String data) {
        if ((data != null) && (data.startsWith("{D}")) && (data.endsWith("{G}"))) {
            return org.apache.commons.lang3.StringUtils.substringBetween(data, "{D}", "{G}");
        }
        throw new RuntimeException("Decryption failed.");
    }

    static {
        try {
            SK = EncryptionTools.getKeyByKeyStore("classpath:orj/worf/security/util/data/Security.sec", Constants.KSP,
                    "SecurityKey", Constants.SKP);
            IV = EncryptionTools.getKeyByKeyStore("classpath:orj/worf/security/util/data/Security.sec", Constants.KSP,
                    "IvKey", Constants.IVP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}