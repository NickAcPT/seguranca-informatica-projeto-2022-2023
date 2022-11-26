package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

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
    public static CipherResult cipherBlob(byte[] blob, byte[] key) throws GeneralSecurityException {
        return cipherBlob(blob, new SecretKeySpec(key, KEY_ALGORITHM));
    }

    /**
     * Cipher a byte array using a key and return the ciphered byte array with the IV as result.
     */
    @NotNull
    public static CipherResult cipherBlob(byte[] blob, SecretKey secretKeySpec) throws GeneralSecurityException {
        Cipher cipher = prepareCipher(secretKeySpec, Cipher.ENCRYPT_MODE);

        return new CipherResult(cipher.getIV(), cipher.doFinal(blob));
    }

    public static byte[] decipherBlob(CipherResult input, SecretKey cipherKey) throws GeneralSecurityException {
        Cipher cipher = prepareCipher(cipherKey, Cipher.DECRYPT_MODE, input.iv());

        return cipher.doFinal(input.blob());
    }

    @NotNull
    private static Cipher prepareCipher(SecretKey secretKeySpec, int mode) throws GeneralSecurityException {
        return prepareCipher(secretKeySpec, mode, null);
    }

    @NotNull
    private static Cipher prepareCipher(SecretKey secretKeySpec, int mode, byte @Nullable [] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(mode, secretKeySpec, iv == null ? null : new IvParameterSpec(iv));
        return cipher;
    }

    public record CipherResult(byte[] blob, byte[] iv) {
    }
}
