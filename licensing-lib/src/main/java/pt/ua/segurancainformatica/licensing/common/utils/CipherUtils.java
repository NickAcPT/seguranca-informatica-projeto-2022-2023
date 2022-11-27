package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public class CipherUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public CipherUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generate a key and return it as a byte array.
     */
    public static byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE);

        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }

    /**
     * Cipher a byte array using a key and return the ciphered byte array with the IV as result.
     */
    public static CipherResult cipherBlob(byte[] blob, byte[] key, String keyAlgorithm) throws GeneralSecurityException {
        return cipherBlob(blob, getKeySpec(key, keyAlgorithm));
    }

    @NotNull
    private static Key getKeySpec(byte[] key, String keyAlgorithm) throws GeneralSecurityException {
        if (Objects.equals(keyAlgorithm, "RSA")) {
            KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
            return publicKeyFactory.generatePublic(new X509EncodedKeySpec(key, keyAlgorithm));
        }
        return new SecretKeySpec(key, keyAlgorithm);
    }

    /**
     * Cipher a byte array using a key and return the ciphered byte array with the IV as result.
     */
    @NotNull
    public static CipherResult cipherBlob(byte[] blob, @NotNull Key secretKeySpec) throws GeneralSecurityException {
        Cipher cipher = prepareCipher(secretKeySpec, Cipher.ENCRYPT_MODE);

        return new CipherResult(cipher.doFinal(blob), cipher.getIV());
    }

    public static byte[] decipherBlob(CipherResult input, byte[] key, String keyAlgorithm) throws GeneralSecurityException {
        return decipherBlob(input, getKeySpec(key, keyAlgorithm));
    }

    public static byte[] decipherBlob(CipherResult input, Key cipherKey) throws GeneralSecurityException {
        Cipher cipher = prepareCipher(cipherKey, Cipher.DECRYPT_MODE, input.iv());

        return cipher.doFinal(input.blob());
    }

    @NotNull
    private static Cipher prepareCipher(Key secretKeySpec, int mode) throws GeneralSecurityException {
        return prepareCipher(secretKeySpec, mode, null);
    }

    @NotNull
    private static Cipher prepareCipher(Key secretKeySpec, int mode, byte @Nullable [] iv) throws GeneralSecurityException {
        String algorithm = Objects.equals(secretKeySpec.getAlgorithm(), "RSA") ? "RSA" : CIPHER_ALGORITHM;
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(mode, secretKeySpec, iv == null ? null : new IvParameterSpec(iv));
        return cipher;
    }

    public record CipherResult(byte[] blob, byte @Nullable [] iv) {
    }
}
