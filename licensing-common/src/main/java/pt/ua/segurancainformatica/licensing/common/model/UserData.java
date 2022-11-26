package pt.ua.segurancainformatica.licensing.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public record UserData(
        String fullName,
        String civilNumber,
        byte[] encodedPublicKey) {

    public UserData(String fullName, String civilNumber, PublicKey publicKey) {
        this(fullName, civilNumber, publicKey.getEncoded());
    }

    @JsonIgnore
    public @Nullable PublicKey publicKey() throws InvalidKeySpecException {
        var spec = new X509EncodedKeySpec(encodedPublicKey);
        try {
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (NoSuchAlgorithmException e) { // wat
            throw new RuntimeException(e);
        }
    }
}
