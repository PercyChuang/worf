package orj.worf.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class UrlCodecUtils {

    public static String arrayToString(final Object[] array, final String delimiter) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder b = new StringBuilder(64);
        for (int i = 0, len = array.length; i < len; i++) {
            if (i > 0) {
                b.append(delimiter);
            }
            b.append(array[i]);
        }
        return b.toString();
    }

    public static String base64Decode(final String base64String) {
        if (StringUtils.isBlank(base64String)) {
            return base64String;
        }
        try {
            return new String(decodeBase64(base64String), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return new String(decodeBase64(base64String));
    }

    public static byte[] decodeBase64(final String base64String) {
        return new Base64(true).decode(base64String);
    }

    public static byte[] encodeBase64(final String base64String) {
        return new Base64(true).encode(base64String.getBytes());
    }

    public static String base64Encode(final String base64String) {
        if (StringUtils.isBlank(base64String)) {
            return base64String;
        }
        try {
            return new String(encodeBase64(base64String), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return new String(encodeBase64(base64String));
    }
}
