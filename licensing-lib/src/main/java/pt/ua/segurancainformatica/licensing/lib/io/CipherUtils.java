package pt.ua.segurancainformatica.licensing.lib.io;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
     * @throws NoSuchAlgorithmException
     * @throws IOException
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
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public void cypherFile(String keyFile, String file) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Files.readAllBytes(Path.of(keyFile)), "AES");
        secretKeySpec.getEncoded();
        byte[] iv= new byte[16];
        new SecureRandom().nextBytes(iv);
        FileInputStream fis=null;
        FileOutputStream fos=null;
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        ivParameterSpec.getIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
        Files.write(Path.of("iv"),iv);
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream("certificate");
            byte[] byteStorage =fis.readAllBytes();
            fos.write(cipher.doFinal(byteStorage));
        }catch (Exception exception){
            fis.close();
            fos.close();
            exception.printStackTrace();
        }
    }

}
