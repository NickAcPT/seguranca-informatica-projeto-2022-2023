package pt.ua.segurancainformatica.licensing.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public record UserData(
        @NotNull String fullName,
        @NotNull String civilNumber,
        byte @NotNull [] encodedPublicKey) {

    public UserData(@NotNull String fullName, @NotNull String civilNumber, @NotNull PublicKey publicKey) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData userData)) return false;

        if (!fullName.equals(userData.fullName)) return false;
        if (!civilNumber.equals(userData.civilNumber)) return false;
        return Arrays.equals(encodedPublicKey, userData.encodedPublicKey);
    }

    @Override
    public int hashCode() {
        int result = fullName.hashCode();
        result = 31 * result + civilNumber.hashCode();
        result = 31 * result + Arrays.hashCode(encodedPublicKey);
        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "fullName='" + fullName + '\'' +
                ", civilNumber='" + civilNumber + '\'' +
                ", encodedPublicKey=" + Arrays.toString(encodedPublicKey) +
                '}';
    }
}
