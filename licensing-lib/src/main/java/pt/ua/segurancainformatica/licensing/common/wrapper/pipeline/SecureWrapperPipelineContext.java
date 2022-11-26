package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;

public record SecureWrapperPipelineContext(
        Class<?> type,
        PublicKey managerPublicKey,
        KeyPair userKeyPair,
        SecretKey cipherKey
) {
}
