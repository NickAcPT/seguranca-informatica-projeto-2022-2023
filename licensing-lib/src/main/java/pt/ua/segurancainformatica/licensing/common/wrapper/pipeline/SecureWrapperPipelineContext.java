package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

public record SecureWrapperPipelineContext(
        Class<?> type,
        PrivateKey signingKey,
        SecretKey cipherKey
) {
}
