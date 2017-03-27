package orj.worf.security.util;

public final class EncryptionUtils {
    public static String decryptFromJsRSA(String data) throws Exception {
        return RSAUtils.decryptFromRSAJS(data);
    }

    public static String encodePassword(String password, String salt) throws Exception {
        return SecurityUtils.encodePassword(password, salt);
    }

    public static String encryptByAES(String data) throws Exception {
        return SecurityUtils.encryptByAES(data);
    }

    public static String decryptByAES(String data) throws Exception {
        return SecurityUtils.decryptByAES(data);
    }

    public static String encryptByPublicKey(String data) throws Exception {
        return SecurityUtils.encryptByPublicKey(data);
    }

    public static String decryptByPrivateKey(String data) throws Exception {
        return SecurityUtils.decryptByPrivateKey(data);
    }

    public static String decryptFromMobileRSA(String data) throws Exception {
        return RSAUtils.decryptFromMobileRSA(data);
    }

    public static String transferToJSRSA(String cipherText) throws Exception {
        return RSAUtils.transferToJSRSA(cipherText);
    }
}