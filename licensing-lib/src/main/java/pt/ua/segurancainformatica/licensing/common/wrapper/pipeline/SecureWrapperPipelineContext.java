package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import javax.crypto.SecretKey;
import java.security.KeyPair;

public record SecureWrapperPipelineContext(
        Class<?> type,
        KeyPair signingKey,
        SecretKey cipherKey
) {
}
