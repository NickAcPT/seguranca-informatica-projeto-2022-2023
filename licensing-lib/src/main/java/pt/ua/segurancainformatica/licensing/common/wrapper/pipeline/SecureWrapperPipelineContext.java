package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Objects;

public final class SecureWrapperPipelineContext {
    private final Class<?> type;
    private final KeyPair managerKeyPair;
    private @Nullable KeyPair userKeyPair;
    private @Nullable SecretKey cipherKey;

    public SecureWrapperPipelineContext(
            Class<?> type,
            KeyPair managerKeyPair,
            @Nullable KeyPair userKeyPair,
            @Nullable SecretKey cipherKey
    ) {
        this.type = type;
        this.managerKeyPair = managerKeyPair;
        this.userKeyPair = userKeyPair;
        this.cipherKey = cipherKey;
    }

    public Class<?> type() {
        return type;
    }

    public KeyPair managerKeyPair() {
        return managerKeyPair;
    }

    public @Nullable KeyPair userKeyPair() {
        return userKeyPair;
    }

    public void userKeyPair(@NotNull KeyPair userKeyPair) {
        this.userKeyPair = userKeyPair;
    }

    public @Nullable SecretKey cipherKey() {
        return cipherKey;
    }

    public void cipherKey(@NotNull SecretKey cipherKey) {
        this.cipherKey = cipherKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SecureWrapperPipelineContext) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.managerKeyPair, that.managerKeyPair) &&
                Objects.equals(this.userKeyPair, that.userKeyPair) &&
                Objects.equals(this.cipherKey, that.cipherKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, managerKeyPair, userKeyPair, cipherKey);
    }

    @Override
    public String toString() {
        return "SecureWrapperPipelineContext[" +
                "type=" + type + ", " +
                "managerKeyPair=" + managerKeyPair + ", " +
                "userKeyPair=" + userKeyPair + ", " +
                "cipherKey=" + cipherKey + ']';
    }
}
