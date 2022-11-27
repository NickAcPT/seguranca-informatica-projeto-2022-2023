package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtils {
    private KeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static PublicKey getPublicKeyFromBytes(byte @NotNull [] key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
    }
}
