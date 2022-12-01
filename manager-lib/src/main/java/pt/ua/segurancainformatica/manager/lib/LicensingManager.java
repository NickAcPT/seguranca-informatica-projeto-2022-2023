package pt.ua.segurancainformatica.manager.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.licensing.common.utils.KeyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.security.*;

public class LicensingManager {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private static @Nullable PublicKey loadedPublicKey;

    private LicensingManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generates a new keypair for the application.
     *
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     */
    public static void generateKeyPair() throws NoSuchAlgorithmException, IOException {
        var keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        var keyPair = keyPairGenerator.generateKeyPair();

        Files.write(ManagerLicensingConstants.PUBLIC_KEY_PATH, keyPair.getPublic().getEncoded());
        Files.write(ManagerLicensingConstants.PRIVATE_KEY_PATH, keyPair.getPrivate().getEncoded());
    }

    /**
     * Get the public key of the application.
     *
     * @return The public key.
     */
    public static @NotNull PublicKey getPublicKey() {
        if (loadedPublicKey != null) {
            return loadedPublicKey;
        }

        try {
            var encodedPublicKey = Files.readAllBytes(ManagerLicensingConstants.PUBLIC_KEY_PATH);
            loadedPublicKey = KeyUtils.getPublicKeyFromBytes(encodedPublicKey);
            return loadedPublicKey;
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Unable to read public key", e);
        }
    }

    /**
     * Get the private key of the application.
     *
     * @return The private key.
     */
    public static @NotNull PrivateKey getPrivateKey() {
        try {
            var encodedPrivateKey = Files.readAllBytes(ManagerLicensingConstants.PRIVATE_KEY_PATH);
            return KeyUtils.getPrivateKeyFromBytes(encodedPrivateKey);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
