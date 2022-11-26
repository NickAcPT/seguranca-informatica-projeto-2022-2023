package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.*;

public class SignatureUtils {
    private SignatureUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Signs the given data with the given private key.
     * @param card The Citizen Card to use.
     * @param blob The data to sign.
     * @return The signature.
     */
    public static byte @Nullable [] signBlob(@NotNull PrivateKey key, byte @NotNull [] blob) {
        try {
            Signature sha256withRSA = Signature.getInstance("SHA256withRSA");
            sha256withRSA.initSign(key);
            sha256withRSA.update(blob);

            return sha256withRSA.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return null;
        }
    }
}
