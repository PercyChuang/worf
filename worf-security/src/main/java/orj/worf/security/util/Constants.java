package orj.worf.security.util;

import orj.worf.util.Bytes;
import orj.worf.util.StringUtils;

class Constants {
    static final String KEYSTORE_TYPE = "JCEKS";
    static final String RSA_KEY_ALGORITHM_NAME = "RSA";
    static final String AES_KEY_ALGORITHM_NAME = "AES";
    static final String AES_PADDING_ALGORITHM_NAME = "AES/CTR/NoPadding";
    static final String HMAC_MD5_ALGORITHM_NAME = "HmacMD5";
    static final String HMAC_SHA224_ALGORITHM_NAME = "HmacSHA224";
    static final String HMAC_SHA512_ALGORITHM_NAME = "HmacSHA512";
    static final String CERTIFICATE_TYPE = "X.509";
    static final String SK = "SecurityKey";
    static final String IV = "IvKey";
    static final String DK = "DefaultKey";
    static final String KS_PATH = "classpath:orj/worf/security/util/data/Security.sec";
    static final String DEFAULT_PUB_PATH = "classpath:orj/worf/security/util/data/Sensitive.pub";
    static final String RSA_1024_PATH = "classpath:orj/worf/security/util/data/RSA1024.dat";
    static final char[] KSP = transfer("4b657953746f726531323324255e262a2829").toCharArray();
    static final char[] SKP = transfer("536b5061737377307264214023343536").toCharArray();
    static final char[] IVP = transfer("49764b5061737377307264214023343536").toCharArray();
    static final char[] DKP = transfer("444b506173737730726431323324255e37382829").toCharArray();
    static final String IOS_RSA_PTE_PATH = "classpath:orj/worf/security/util/data/ios/java_private_key.pem";
    static final String IOS_RSA_PUB_PATH = "classpath:orj/worf/security/util/data/ios/java_public_key.pem";

    private static String transfer(String data) {
        return StringUtils.newStringUtf8(Bytes.hex2bytes(data));
    }
}