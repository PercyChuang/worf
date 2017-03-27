package orj.worf.security.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
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
import orj.worf.util.Hex;
import orj.worf.util.ResourceUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.Resource;

final class EncryptionTools {
    private static final IvParameterSpec DEFAULT_IVS = new IvParameterSpec(new byte[] { 11, -19, 16, 33, -68, 88, 11,
            20, 24, 35, 68, 23, 60, 24, 29, 67 });

    public static SecretKey initHmacKey(String algorithmName, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithmName);
        generator.init(keySize);
        return generator.generateKey();
    }

    public static SecretKey initHmacSHA224Key(int keySize) throws NoSuchAlgorithmException {
        return initHmacKey("HmacSHA224", keySize);
    }

    public static SecretKey initHmacSHA512Key(int keySize) throws NoSuchAlgorithmException {
        return initHmacKey("HmacSHA512", keySize);
    }

    public static SecretKey initHmacMD5Key(int keySize) throws NoSuchAlgorithmException {
        return initHmacKey("HmacMD5", keySize);
    }

    public static SecretKey initGeneralPasswordKey(int keySize) throws NoSuchAlgorithmException {
        return initHmacSHA224Key(keySize);
    }

    public static SecretKey initStrongPasswordKey(int keySize) throws NoSuchAlgorithmException {
        return initHmacSHA512Key(keySize);
    }

    public static SecretKey initSensitiveInformationKey(int keySize) throws NoSuchAlgorithmException {
        return initHmacMD5Key(keySize);
    }

    private static byte[] initSensitiveInformationBidirectionalByteKey() throws Exception {
        SecretKey key = initSensitiveInformationBidirectionalKey();
        return key.getEncoded();
    }

    private static SecretKey initSensitiveInformationBidirectionalKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");

        generator.init(128);
        return generator.generateKey();
    }

    public static byte[] encodeHmac(String algorithmName, String data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithmName);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static byte[] encodeHmac(String algorithmName, byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, algorithmName);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static byte[] encodeGeneralPassword(String password, byte[] key) throws Exception {
        return encodeHmac("HmacSHA224", password, key);
    }

    public static byte[] encodeStrongPassword(String password, byte[] key) throws Exception {
        return encodeHmac("HmacSHA512", password, key);
    }

    public static byte[] encodeStrongPassword(byte[] password, byte[] key) throws Exception {
        return encodeHmac("HmacSHA512", password, key);
    }

    public static byte[] encodeStrongPassword(String password, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacSHA512", password, key);
    }

    public static byte[] encodeStrongPassword(byte[] password, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacSHA512", password, key);
    }

    public static byte[] encodeGeneralPassword(String password, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacSHA224", password, key);
    }

    public static byte[] encodeGeneralPassword(byte[] password, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacSHA224", password, key);
    }

    public static byte[] encodeGeneralPassword(byte[] password, byte[] key) throws Exception {
        return encodeHmac("HmacSHA224", password, key);
    }

    public static byte[] encodeSensitiveInformation(String rawSensitiveInformation, String keyStorePath,
            char[] keyStorePassword, String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacMD5", rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(byte[] rawSensitiveInformation, String keyStorePath,
            char[] keyStorePassword, String alias, char[] keyPassword) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encodeHmac("HmacMD5", rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(byte[] rawSensitiveInformation, byte[] key) throws Exception {
        return encodeHmac("HmacMD5", rawSensitiveInformation, key);
    }

    public static byte[] encodeSensitiveInformation(String rawSensitiveInformation, byte[] key) throws Exception {
        return encodeHmac("HmacMD5", rawSensitiveInformation, key);
    }

    public static SecretKey toKey(byte[] key, String algorithmName) {
        return new SecretKeySpec(key, algorithmName);
    }

    public static byte[] encryptSensitiveInformation(byte[] data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encryptSensitiveInformation(data, key, ivs);
    }

    public static byte[] encryptSensitiveInformation(byte[] data, byte[] key, byte[] ivs) throws Exception {
        Key k = toKey(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(1, k, new IvParameterSpec(ivs));
        return cipher.doFinal(data);
    }

    public static byte[] encryptSensitiveInformation(String data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return encryptSensitiveInformation(data, key, ivs);
    }

    public static byte[] encryptSensitiveInformation(String data, byte[] key, byte[] ivs) throws Exception {
        Key k = toKey(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(1, k, new IvParameterSpec(ivs));
        return cipher.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static byte[] decryptSensitiveInformation(byte[] data, byte[] key, byte[] ivs) throws Exception {
        Key k = toKey(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(2, k, new IvParameterSpec(ivs));
        return cipher.doFinal(data);
    }

    public static String decryptSensitiveInformationToString(byte[] data, byte[] key, byte[] ivs) throws Exception {
        byte[] info = decryptSensitiveInformation(data, key, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(byte[] data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return decryptSensitiveInformation(data, key, ivs);
    }

    public static String decryptSensitiveInformationToString(byte[] data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] info = decryptSensitiveInformation(data, keyStorePath, keyStorePassword, alias, keyPassword, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(String data, byte[] key, byte[] ivs) throws Exception {
        Key k = toKey(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(2, k, new IvParameterSpec(ivs));
        return cipher.doFinal(StringUtils.getBytesUtf8(data));
    }

    public static String decryptSensitiveInformationToString(String data, byte[] key, byte[] ivs) throws Exception {
        byte[] info = decryptSensitiveInformation(data, key, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static byte[] decryptSensitiveInformation(String data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] key = getKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        return decryptSensitiveInformation(data, key, ivs);
    }

    public static String decryptSensitiveInformationToString(String data, String keyStorePath, char[] keyStorePassword,
            String alias, char[] keyPassword, byte[] ivs) throws Exception {
        byte[] info = decryptSensitiveInformation(data, keyStorePath, keyStorePassword, alias, keyPassword, ivs);
        return StringUtils.newStringUtf8(info);
    }

    public static KeyStore loadKeyStore(String keyStorePath, char[] password, String keystoreType) throws Exception {
        KeyStore ks = KeyStore.getInstance((keystoreType == null) ? KeyStore.getDefaultType() : keystoreType);
        Resource[] resources = ResourceUtils.getResources(keyStorePath);
        if (resources.length == 1) {
            InputStream is = null;
            try {
                is = resources[0].getInputStream();
                ks.load(is, password);
                KeyStore localKeyStore1 = ks;

                return localKeyStore1;
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

    public static byte[] getKeyByKeyStore(String keyStorePath, char[] keyStorePassword, String alias, char[] keyPassword)
            throws Exception {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword, "JCEKS");
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias,
                new KeyStore.PasswordProtection(keyPassword));

        return entry.getSecretKey().getEncoded();
    }

    public static Certificate getCertificate(String certificatePath) throws Exception {
        Resource[] resources = ResourceUtils.getResources(certificatePath);
        if (resources.length == 1) {
            InputStream inputStream = null;
            try {
                inputStream = resources[0].getInputStream();
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate localCertificate = cf.generateCertificate(inputStream);

                return localCertificate;
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

    public static Certificate getCertificate(byte[] certificateContent) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(certificateContent);
        Throwable localThrowable2 = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(inputStream);
            Certificate localCertificate1 = cert;
            return localCertificate1;
        } catch (Throwable localThrowable1) {
        } finally {
            if (inputStream != null)
                if (localThrowable2 != null)
                    try {
                        inputStream.close();
                    } catch (Throwable x2) {
                        localThrowable2.addSuppressed(x2);
                    }
                else
                    inputStream.close();
        }

        return null;
    }

    public static Certificate getCertificate(String keyStorePath, char[] keyStorePassword, String alias)
            throws Exception {
        return getCertificate("JCEKS", keyStorePath, keyStorePassword, alias);
    }

    public static Certificate getCertificate(String keystoreType, String keyStorePath, char[] keyStorePassword,
            String alias) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null) {
            List aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1)
                alias = (String) aliases.get(0);
            else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }

        return ks.getCertificate(alias);
    }

    public static PrivateKey getPrivateKeyByKeyStore(String keyStorePath, char[] keyStorePassword, String alias,
            char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, "JCEKS");
        if (alias == null) {
            List aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1)
                alias = (String) aliases.get(0);
            else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }

        return ((PrivateKey) ks.getKey(alias, keyPassword));
    }

    public static PublicKey getPublicKeyByCertificate(String certificatePath) throws Exception {
        Certificate x509Certificate = getCertificate(certificatePath);
        return x509Certificate.getPublicKey();
    }

    public static PublicKey getPublicKeyByCertificate(byte[] certificateContent) throws Exception {
        Certificate cert = getCertificate(certificateContent);
        return cert.getPublicKey();
    }

    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, char[] keyStorePassword, String alias,
            char[] keyPassword) throws Exception {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(1, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(2, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(1, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, char[] keyStorePassword, String alias,
            char[] keyPassword) throws Exception {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] signByX509Certificate(byte[] data, String keyStorePath, char[] keyStorePassword, String alias,
            char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, "JCEKS");
        if (alias == null) {
            List aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1)
                alias = (String) aliases.get(0);
            else {
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

    public static byte[] signByX509Certificate(String keystoreType, byte[] data, String keyStorePath,
            char[] keyStorePassword, String alias, char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null) {
            List aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1)
                alias = (String) aliases.get(0);
            else {
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

    public static byte[] signByX509Certificate(String keystoreType, String sigAlgName, byte[] data,
            String keyStorePath, char[] keyStorePassword, String alias, char[] keyPassword) throws Exception {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        Signature signature = Signature.getInstance(sigAlgName);
        if (alias == null) {
            List aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1)
                alias = (String) aliases.get(0);
            else {
                throw new IllegalArgumentException(
                        "[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] signByX509Certificate(byte[] data, String keyStorePath, char[] keyStorePassword, String alias,
            char[] keyPassword, X509Certificate x509Certificate) throws Exception {
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, keyStorePassword, alias, keyPassword);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verifySign(byte[] data, byte[] sign, String keyStorePath, char[] keyStorePassword,
            String alias) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(keyStorePath, keyStorePassword, alias);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(byte[] data, byte[] sign, String keystoreType, String keyStorePath,
            char[] keyStorePassword, String alias) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(keystoreType, keyStorePath, keyStorePassword,
                alias);

        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(byte[] data, byte[] sign, String certificatePath) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(certificatePath);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifySign(byte[] plaintext, byte[] signature, byte[] certificateContent) throws Exception {
        X509Certificate certificate = (X509Certificate) getCertificate(certificateContent);
        Signature verifier = Signature.getInstance(certificate.getSigAlgName());
        verifier.initVerify(certificate);
        verifier.update(plaintext);
        return verifier.verify(signature);
    }

    public static boolean verifySign(byte[] plaintext, byte[] signature, Certificate certificate) throws Exception {
        X509Certificate x509certificate = (X509Certificate) certificate;
        Signature verifier = Signature.getInstance(x509certificate.getSigAlgName());
        verifier.initVerify(x509certificate);
        verifier.update(plaintext);
        return verifier.verify(signature);
    }

    public static String encodeBase64(byte[] binaryData) {
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    public static String encodeBase64(String string) {
        return Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8(string));
    }

    public static String decodeBase64(String base64String) {
        byte[] decoded = Base64.decodeBase64(base64String);
        return StringUtils.newStringUtf8(decoded);
    }

    public static byte[] getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        FileInputStream in = new FileInputStream(file);
        Throwable localThrowable2 = null;
        try {
            FileChannel ch = in.getChannel();
            int maxSize = 700000000;
            long startPosition = 0L;
            long step = file.length() / maxSize;
            MessageDigest md5MessageDigest = null;
            md5MessageDigest = MessageDigest.getInstance("MD5");
            if (step == 0L) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                md5MessageDigest.update(byteBuffer);
                byte[] arrayOfByte = md5MessageDigest.digest();
                return arrayOfByte;
            }
            for (int i = 0; i < step; ++i) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, maxSize);
                md5MessageDigest.update(byteBuffer);
                startPosition += maxSize;
            }
            if (startPosition == file.length()) {
                byte[] i = md5MessageDigest.digest();
                return i;
            }
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, file.length()
                    - startPosition);

            md5MessageDigest.update(byteBuffer);
            byte[] x2 = md5MessageDigest.digest();
            return x2;
        } catch (Throwable localThrowable1) {
        } finally {
            if (in != null)
                if (localThrowable2 != null)
                    try {
                        in.close();
                    } catch (Throwable x2) {
                        localThrowable2.addSuppressed(x2);
                    }
                else
                    in.close();
        }

        return null;
    }

    public static String getFileMD5Hex(File file) throws IOException, NoSuchAlgorithmException {
        byte[] md5 = getFileMD5(file);
        return Hex.encodeHexString(md5);
    }

    public static String getFileMD5Base64(File file, String charsetName) throws IOException, NoSuchAlgorithmException {
        byte[] md5 = getFileMD5(file);
        byte[] encodeBase64 = Base64.encodeBase64(md5, false, true);
        return StringUtils.newString(encodeBase64, charsetName);
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}