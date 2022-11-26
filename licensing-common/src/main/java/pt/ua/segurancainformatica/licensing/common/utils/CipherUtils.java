package pt.ua.segurancainformatica.licensing.common.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Generate a key and return it as a byte array.
     */
    public byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE);

        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }

    /**
     * Cipher a byte array using a key and return the ciphered byte array with the IV as result.
     */
    public CipherResult cipherBlob(byte[] key, byte[] blob) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return new CipherResult(cipher.getIV(), cipher.doFinal(blob));
    }

    public record CipherResult(byte[] iv, byte[] encrypted) {
    }
}
