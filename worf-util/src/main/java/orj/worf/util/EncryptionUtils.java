package orj.worf.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import orj.worf.core.util.ResourceUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.Resource;

public class EncryptionUtils {
    public static final String TRIPLE_DES_ALGORITHM_NAME = "DESede";
    public static final String AES_KEY_ALGORITHM_NAME = "AES";
    public static final String AES_CIPHER_ALGORITHM_NAME = "AES/CTR/PKCS7Padding";
    public static final String HMAC_MD5_ALGORITHM_NAME = "HmacMD5";
    public static final String HMAC_SHA224_ALGORITHM_NAME = "HmacSHA224";
    public static final String HMAC_SHA512_ALGORITHM_NAME = "HmacSHA512";
    public static final String TRIPLE_DES_TRANSFORMATION_NAME = "DESede/ECB/PKCS5Padding";
    public static final String CERTIFICATE_TYPE = "X.509";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String RSA_SHA1_SIGNATURE_ALGORITHM_NAME = "SHA1WithRSA";
    public static final String RSA_MD5_SIGNATURE_ALGORITHM_NAME = "MD5withRSA";
    private static final IvParameterSpec DEFAULT_IVS = new IvParameterSpec(new byte[] { 11, -19, 16, 33, -68, 88, 11,
            20, 24, 35, 68, 23, 60, 24, 29, 67 });
    public static final String DEFAULT_STRING_OUTPUT_TYPE = "base64";
    private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SecretKey initHmacKey(final String algorithmName, final int keySize) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithmName);
        generator.init(keySize);
        SecretKey key = generator.generateKey();
        return key;
    }

    public static SecretKey initHmacSHA224Key(final int keySize) throws NoSuchAlgorithmException {
        return initHmacKey(HMAC_SHA224_ALGORITHM_NAME, keySize);
    }

    public static SecretKey initHmacSHA512Key(final int keySize) throws NoSuchAlgorithmException {
        return initHmacKey(HMAC_SHA512_ALGORITHM_NAME, keySize);
    }

    public static SecretKey initHmacMD5Key(final int keySize) throws NoSuchAlgorithmException {
        return initHmacKey(HMAC_MD5_ALGORITHM_NAME, keySize);
    }

    public static SecretKey initGeneralPasswordKey(final int keySize) throws NoSuchAlgorithmException {
        return initHmacSHA224Key(keySize);
    }

    public static SecretKey initStrongPasswordKey(final int keySize) throws NoSuchAlgorithmException {
        return initHmacSHA512Key(keySize);
    }

    public static SecretKey initSensitiveInformationKey(final int keySize) throws NoSuchAlgorithmException {
        return initHmacMD5Key(keySize);
    }

    public static byte[] initSensitiveInformationBidirectionalByteKey() throws Exception {
        SecretKey key = initSensitiveInformationBidirectionalKey();
        return key.getEncoded();
    }

    public static SecretKey initSensitiveInformationBidirectionalKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(AES_KEY_ALGORITHM_NAME, "BC");
        generator.init(128);
        SecretKey key = generator.generateKey();
        return key;
    }

    public static byte[] encodeHmac(final String algorithmName, final String data, final byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithmName);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static byte[] encodeHmac(final String algorithmName, final byte[] data, final byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithmName);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] encodeGeneralPassword(final String password, final byte[] key) throws Exception {
        return encodeHmac(HMAC_SHA224_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeStrongPassword(final String password, final byte[] key) throws Exception {
        return encodeHmac(HMAC_SHA512_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeStrongPassword(final byte[] password, final byte[] key) throws Exception {
        return encodeHmac(HMAC_SHA512_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeStrongPassword(final String password, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_SHA512_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeStrongPassword(final byte[] password, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_SHA512_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeGeneralPassword(final String password, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_SHA224_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeGeneralPassword(final byte[] password, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_SHA224_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeGeneralPassword(final byte[] password, final byte[] key) throws Exception {
        return encodeHmac(HMAC_SHA224_ALGORITHM_NAME, password, key);
    }

    public static byte[] encodeSensitiveInformation(final String rawSensitiveInformation, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_MD5_ALGORITHM_NAME, rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(final byte[] rawSensitiveInformation, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encodeHmac(HMAC_MD5_ALGORITHM_NAME, rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(final byte[] rawSensitiveInformation, final byte[] key)
            throws Exception {
        return encodeHmac(HMAC_MD5_ALGORITHM_NAME, rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(final String rawSensitiveInformation, final byte[] key)
            throws Exception {
        return encodeHmac(HMAC_MD5_ALGORITHM_NAME, rawSensitiveInformation, key);
    }

    public static SecretKey toKey(final byte[] key, final String algorithmName) {
        return new SecretKeySpec(key, algorithmName);
    }

    public static byte[] encryptSensitiveInformation(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encryptSensitiveInformation(data, key, ivs);
    }

    public static byte[] encryptSensitiveInformation(final byte[] data, final byte[] key, final byte[] ivs)
            throws Exception {
        Key k = toKey(key, AES_KEY_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_NAME, "BC");
        cipher.init(1, k, ivs == null ? DEFAULT_IVS : new IvParameterSpec(ivs));
        return cipher.doFinal(data);
    }

    public static byte[] encryptSensitiveInformation(final String data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return encryptSensitiveInformation(data, key, ivs);
    }

    public static byte[] encryptSensitiveInformation(final String data, final byte[] key, final byte[] ivs)
            throws Exception {
        Key k = toKey(key, AES_KEY_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_NAME, "BC");
        cipher.init(1, k, ivs == null ? DEFAULT_IVS : new IvParameterSpec(ivs));
        return cipher.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static byte[] decryptSensitiveInformation(final byte[] data, final byte[] key, final byte[] ivs)
            throws Exception {
        Key k = toKey(key, AES_KEY_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_NAME, "BC");
        cipher.init(2, k, ivs == null ? DEFAULT_IVS : new IvParameterSpec(ivs));
        return cipher.doFinal(data);
    }

    public static String decryptSensitiveInformationToString(final byte[] data, final byte[] key, final byte[] ivs)
            throws Exception {
        byte[] info = decryptSensitiveInformation(data, key, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return decryptSensitiveInformation(data, key, ivs);
    }

    public static String decryptSensitiveInformationToString(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] info = decryptSensitiveInformation(data, keyStorePath, keyStorePassword, alias, keyPasswork, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(final String data, final byte[] key, final byte[] ivs)
            throws Exception {
        Key k = toKey(key, AES_KEY_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM_NAME, "BC");
        cipher.init(2, k, ivs == null ? DEFAULT_IVS : new IvParameterSpec(ivs));
        return cipher.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static String decryptSensitiveInformationToString(final String data, final byte[] key, final byte[] ivs)
            throws Exception {
        byte[] info = decryptSensitiveInformation(data, key, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(final String data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        return decryptSensitiveInformation(data, key, ivs);
    }

    public static String decryptSensitiveInformationToString(final String data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork, final byte[] ivs)
                    throws Exception {
        byte[] info = decryptSensitiveInformation(data, keyStorePath, keyStorePassword, alias, keyPasswork, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static KeyStore loadKeyStore(final String keyStorePath, final char[] password, final String keystoreType)
            throws Exception {
        KeyStore ks = KeyStore.getInstance(keystoreType == null ? KeyStore.getDefaultType() : keystoreType);
        Resource[] resources = ResourceUtils.getResources(keyStorePath);
        if (resources.length == 1) {
            InputStream is = null;
            try {
                is = resources[0].getInputStream();
                ks.load(is, password);
                return ks;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        if (resources.length < 1) {
            throw new IllegalArgumentException("In the path [" + keyStorePath + "], found less than one KeyStore");
        }
        throw new IllegalArgumentException("In the path [" + keyStorePath + "], found more than one KeyStore");
    }

    public static byte[] getKeyByKeyStore(final String keyStorePath, final char[] keyStorePassword, final String alias,
            final char[] keyPasswork) throws Exception {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword, "JCEKS");
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias,
                new KeyStore.PasswordProtection(keyPasswork));
        return entry.getSecretKey().getEncoded();
    }

    public static Certificate getCertificate(final String certificatePath) throws Exception {
        Resource[] resources = ResourceUtils.getResources(certificatePath);
        if (resources.length == 1) {
            InputStream inputStream = null;
            try {
                inputStream = resources[0].getInputStream();
                CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
                Certificate cert = cf.generateCertificate(inputStream);
                Certificate localCertificate1 = cert;
                return localCertificate1;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        if (resources.length < 1) {
            throw new IllegalArgumentException("In the path [" + certificatePath + "], found less than one certificate");
        }
        throw new IllegalArgumentException("In the path [" + certificatePath + "], found more than one certificate");
    }

    public static Certificate getCertificate(final byte[] certificateContent) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(certificateContent);
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            Certificate cert = cf.generateCertificate(inputStream);
            return cert;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static Certificate getCertificate(final String keyStorePath, final char[] keyStorePassword,
            final String alias) throws Exception {
        return getCertificate(null, keyStorePath, keyStorePassword, alias);
    }

    public static Certificate getCertificate(final String keystoreType, final String keyStorePath,
            final char[] keyStorePassword, String alias) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            } else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        return ks.getCertificate(alias);
    }

    public static PrivateKey getPrivateKeyByKeyStore(final String keyStorePath, final char[] keyStorePassword,
            String alias, final char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, null);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            } else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        PrivateKey key = (PrivateKey) ks.getKey(alias, keyPassword);
        return key;
    }

    public static PublicKey getPublicKeyByCertificate(final String certificatePath) throws Exception {
        Certificate x509Certificate = getCertificate(certificatePath);
        return x509Certificate.getPublicKey();
    }

    public static PublicKey getPublicKeyByCertificate(final byte[] certificateContent) throws Exception {
        Certificate cert = getCertificate(certificateContent);
        return cert.getPublicKey();
    }

    public static byte[] encryptByPrivateKey(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(1, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(final byte[] data, final String certificatePath) throws Exception {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(2, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(final byte[] data, final String certificatePath) throws Exception {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(1, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPasswork) throws Exception {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPasswork);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] signByX509Certificate(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, String alias, final char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, null);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            } else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        X509Certificate x509Certificate = (X509Certificate) ks.getCertificate(alias);
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] signByX509Certificate(final String keystoreType, final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, String alias, final char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            } else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        X509Certificate x509Certificate = (X509Certificate) ks.getCertificate(alias);
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] signByX509Certificate(final String keystoreType, final String sigAlgName, final byte[] data,
            final String keyStorePath, final char[] keyStorePassword, String alias, final char[] keyPassword)
                    throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        Signature signature = Signature.getInstance(sigAlgName);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            } else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] signByX509Certificate(final byte[] data, final String keyStorePath,
            final char[] keyStorePassword, final String alias, final char[] keyPassword,
            final X509Certificate x509Certificate) throws Exception {
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verifySign(final byte[] data, final byte[] sign, final String keyStorePath,
            final char[] keyStorePassword, final String alias) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(keyStorePath, keyStorePassword, alias);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(final byte[] data, final byte[] sign, final String keystoreType,
            final String keyStorePath, final char[] keyStorePassword, final String alias) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(keystoreType, keyStorePath, keyStorePassword,
                alias);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(final byte[] data, final byte[] sign, final String certificatePath)
            throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(certificatePath);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(final byte[] plaintext, final byte[] signature, final byte[] certificateContent)
            throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(certificateContent);
        Signature verifier = Signature.getInstance(certificate.getSigAlgName());
        verifier.initVerify(certificate);
        verifier.update(plaintext);
        return verifier.verify(signature);
    }

    public static boolean verifySign(final byte[] plaintext, final byte[] signature, final Certificate certificate)
            throws Exception {
        X509Certificate x509certificate = (X509Certificate) certificate;
        Signature verifier = Signature.getInstance(x509certificate.getSigAlgName());
        verifier.initVerify(x509certificate);
        verifier.update(plaintext);
        return verifier.verify(signature);
    }

    public static boolean isBase64(final String msg) {
        return Base64.isBase64(msg);
    }

    public static boolean isBase64(final byte[] msg) {
        return Base64.isBase64(msg);
    }

    public static boolean isBase64(final byte msg) {
        return Base64.isBase64(msg);
    }

    public static String decodeBase64(final String base64String) {
        byte[] decoded = new Base64().decode(base64String);
        return StringUtils.newStringUtf8(decoded);
    }

    public static byte[] getFileMD5(final File file) throws IOException, NoSuchAlgorithmException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            int maxSize = 700000000;
            long startPosition = 0L;
            long step = file.length() / maxSize;
            MessageDigest md5MessageDigest = null;
            md5MessageDigest = MessageDigest.getInstance("MD5");
            if (step == 0L) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                md5MessageDigest.update(byteBuffer);
                return md5MessageDigest.digest();
            }
            for (int i = 0; i < step; i++) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, maxSize);
                md5MessageDigest.update(byteBuffer);
                startPosition += maxSize;
            }
            if (startPosition == file.length()) {
                return md5MessageDigest.digest();
            }
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, file.length()
                    - startPosition);
            md5MessageDigest.update(byteBuffer);
            byte[] arrayOfByte = md5MessageDigest.digest();
            return arrayOfByte;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static String getFileMD5Hex(final File file) throws IOException, NoSuchAlgorithmException {
        byte[] md5 = getFileMD5(file);
        return bufferToHex(md5);
    }

    public static String getFileMD5Base64(final File file, final String charsetName) throws IOException,
    NoSuchAlgorithmException {
        byte[] md5 = getFileMD5(file);
        byte[] encodeBase64 = Base64.encodeBase64(md5, false, true);
        String newString = StringUtils.newString(encodeBase64, charsetName);
        return newString;
    }

    private static String bufferToHex(final byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(final byte[] bytes, final int m, final int n) {
        StringBuilder sb = new StringBuilder(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], sb);
        }
        return sb.toString();
    }

    private static void appendHexPair(final byte bt, final StringBuilder sb) {
        char c0 = hexDigits[(bt & 0xF0) >> 4];
        char c1 = hexDigits[bt & 0xF];
        sb.append(c0);
        sb.append(c1);
    }

    public static String md5Encrypt(final String msg, final String charset) throws UnsupportedEncodingException,
    NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(msg.getBytes(charset == null ? DEFAULT_CHARSET : charset));
        byte[] digest = md5.digest();
        return convertToHexString(digest);
    }

    private static String convertToHexString(final byte[] digest) {
        StringBuilder result = new StringBuilder("");
        for (byte element : digest) {
            result.append(Integer.toHexString(0xFF & element | 0xFFFFFF00).substring(6));
        }
        return result.toString();
    }
}
