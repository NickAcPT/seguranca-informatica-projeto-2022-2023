package pt.ua.segurancainformatica.manager.lib;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class LicensingManager {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private LicensingManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generates a new keypair for the application.
     * @return The generated public key.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws KeyStoreException If the keystore is not initialized.
     */
    public static byte[] generateKeyPair() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        var keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        var keyPair = keyPairGenerator.generateKeyPair();

        var instance = KeyStore.getInstance(KeyStore.getDefaultType());
        instance.load(null, null);

        // TODO: Save in keystore

        return keyPair.getPublic().getEncoded();
    }

    /**
     * Get the public key of the application.
     * @return The public key.
     */
    public static byte[] getPublicKey() {
        // TODO: Get from keystore
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Get the private key of the application.
     * @return The private key.
     */
    public static byte[] getPrivateKey() {
        // TODO: Get from keystore
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
