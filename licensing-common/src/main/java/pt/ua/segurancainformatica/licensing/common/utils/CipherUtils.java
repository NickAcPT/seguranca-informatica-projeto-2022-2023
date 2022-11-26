package pt.ua.segurancainformatica.licensing.common.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CipherUtils {

    /**
     * Generate a key inside the file
     * @param keyFile file that you will save your key
     */
    public void keyGenerate(String keyFile) throws NoSuchAlgorithmException, IOException {
        KeyGenerator keyGenerator= KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        Files.write(Path.of(keyFile),encoded);
    }

    /**
     * Cipher a file with a key, generate/modify un iv file and un certificate file ciphered
     * @param keyFile file with key
     * @param file file that will be cipher
     */
    public void cypherFile(String keyFile, String file) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Files.readAllBytes(Path.of(keyFile)), "AES");
        secretKeySpec.getEncoded();
        byte[] iv= new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        ivParameterSpec.getIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
        Files.write(Path.of("iv"),iv);

        try {
            Files.write(Path.of("certificate"), cipher.doFinal(Files.readAllBytes(Path.of(file))));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

}
