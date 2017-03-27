package orj.worf.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomStringGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);
    private static final int DEFAULT_LENGTH = 8;

    private static final char[] AlphaNumberic = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    private static final char[] Alphabetic = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    private static final char[] Numberic = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

    public static String randomString(final int length) {
        int len = DEFAULT_LENGTH;
        if (length > 0) {
            len = length;
        }
        return generator(len, AlphaNumberic);
    }

    private static String generator(final int len, final char[] chars) {
        char[] randomChars = new char[len];
        try {
            SecureRandom wheel = SecureRandom.getInstance("SHA1PRNG");
            for (int i = 0; i < len; i++) {
                int nextChar = wheel.nextInt(chars.length);
                randomChars[i] = chars[nextChar];
            }
            return new String(randomChars);
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e.getMessage());
            return RandomStringUtils.randomAlphanumeric(len);
        }
    }

    public static String randomAlphabetic(final int length) {
        int len = DEFAULT_LENGTH;
        if (length > 0) {
            len = length;
        }
        return generator(len, Alphabetic);
    }

    public static String randomNumeric(final int length) {
        int len = DEFAULT_LENGTH;
        if (length > 0) {
            len = length;
        }
        return generator(len, Numberic);
    }

    public static String randomString() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
    }
}
