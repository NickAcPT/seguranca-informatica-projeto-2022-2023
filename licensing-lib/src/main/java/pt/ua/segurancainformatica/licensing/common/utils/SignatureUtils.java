package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignatureUtils {
    private SignatureUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Signs the given data with the given private key.
     *
     * @param key  The private key to use.
     * @param blob The data to sign.
     * @return The signature.
     */
    public static byte @Nullable [] signBlob(@NotNull PrivateKey key, byte @NotNull [] blob) throws GeneralSecurityException {
        Signature sha256withRSA = Signature.getInstance("SHA256withRSA");
        sha256withRSA.initSign(key);
        sha256withRSA.update(blob);

        return sha256withRSA.sign();
    }

    /**
     * Verifies the given signature for the given data with the given public key.
     *
     * @param key      The public key to use.
     * @param blob     The data to verify.
     * @param signature The signature to verify.
     * @return True if the signature is valid, false otherwise.
     */
    public static boolean verifyBlob(@NotNull PublicKey key, byte @NotNull [] blob, byte @NotNull [] signature) throws GeneralSecurityException {
        Signature sha256withRSA = Signature.getInstance("SHA256withRSA");
        sha256withRSA.initVerify(key);
        sha256withRSA.update(blob);

        return sha256withRSA.verify(signature);
    }
}
