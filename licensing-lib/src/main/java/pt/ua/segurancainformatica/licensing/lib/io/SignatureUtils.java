package pt.ua.segurancainformatica.licensing.lib.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.impl.CitizenCardLibraryImpl;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.security.*;

public class SignatureUtils {

    private static final Provider pkcs11Provider = CitizenCardLibraryImpl.INSTANCE.getProvider();

    private SignatureUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Signs the given data with the given private key.
     * @param card The Citizen Card to use.
     * @param blob The data to sign.
     * @return The signature.
     */
    public static byte @Nullable [] signBlob(@NotNull CitizenCard card, byte @NotNull [] blob) {
        var key = card.getAuthenticationPrivateKey();

        try {
            Signature sha256withRSA = Signature.getInstance("SHA256withRSA", pkcs11Provider);
            sha256withRSA.initSign(key);
            sha256withRSA.update(blob);

            return sha256withRSA.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return null;
        }
    }
}
