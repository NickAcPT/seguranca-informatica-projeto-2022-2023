package pt.ua.segurancainformatica.manager.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.licensing.common.model.license.LicenseData;
import pt.ua.segurancainformatica.licensing.common.model.license.LicenseInformation;
import pt.ua.segurancainformatica.licensing.common.model.request.LicenseRequest;
import pt.ua.segurancainformatica.licensing.common.utils.KeyUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineSide;
import pt.ua.segurancainformatica.manager.lib.licenses.LicenseInformationManager;
import pt.ua.segurancainformatica.manager.lib.release.ApplicationRelease;
import pt.ua.segurancainformatica.manager.lib.release.ApplicationReleaseManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LicensingManager {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private static @Nullable PublicKey loadedPublicKey;

    private LicensingManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check if the key pair has been generated.
     *
     * @return {@code true} if the key pair has been generated, {@code false} otherwise.
     */
    public static boolean isKeyPairGenerated() {
        return Files.exists(ManagerLicensingConstants.PRIVATE_KEY_PATH) &&
                Files.exists(ManagerLicensingConstants.PUBLIC_KEY_PATH);
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

    public static void clearKeyPair() {
        try {
            Files.deleteIfExists(ManagerLicensingConstants.PRIVATE_KEY_PATH);
            Files.deleteIfExists(ManagerLicensingConstants.PUBLIC_KEY_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String getSimpleUserName(LicenseRequest request) {
        var name = request.user().fullName();
        // Split at first space
        var split = name.split(" ", 2);
        return split[0];
    }

    public static void processRequest(byte[] requestBytes) throws SecureWrapperInvalidatedException, LicensingManagerException, IOException {
        var managerKeyPair = new KeyPair(LicensingManager.getPublicKey(), LicensingManager.getPrivateKey());
        var licenseRequestContext = new SecureWrapperPipelineContext<>(
                LicenseRequest.class,
                SecureWrapperPipelineSide.MANAGER, managerKeyPair,
                null, null
        );

        var request = SecureWrapper.unwrapObject(requestBytes, licenseRequestContext);
        var requestApplicationRelease = new ApplicationRelease(request.application().name(), request.application().version(), request.application().hash());

        var releases = ApplicationReleaseManager.INSTANCE.getValues().stream()
                .filter(r -> r.equals(requestApplicationRelease))
                .findFirst().orElse(null);

        if (releases == null) {
            throw new LicensingManagerException("Release for " + requestApplicationRelease + " not found");
        }

        var now = Instant.now();
        var license = new LicenseInformation(
                request.user(),
                request.application(),
                request.computer(),
                new LicenseData(now, now.plus(30, ChronoUnit.DAYS))
        );

        var licenseInfoContext = new SecureWrapperPipelineContext<>(
                LicenseInformation.class,
                SecureWrapperPipelineSide.MANAGER, managerKeyPair,
                licenseRequestContext.userKeyPair(), licenseRequestContext.cipherKey()
        );
        var licenseBytes = SecureWrapper.wrapObject(license, licenseInfoContext);

        Files.write(Path.of(ManagerLicensingConstants.LICENSE_TEMPLATE_NAME.replace("%name%", getSimpleUserName(request))), licenseBytes);

        LicenseInformationManager.INSTANCE.addValue(license);
    }
}
