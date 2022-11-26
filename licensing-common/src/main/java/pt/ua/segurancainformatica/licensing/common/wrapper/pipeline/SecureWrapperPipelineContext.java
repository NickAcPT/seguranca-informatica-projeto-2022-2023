package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import com.fasterxml.jackson.core.type.TypeReference;

import java.security.PrivateKey;

public record SecureWrapperPipelineContext(
        TypeReference<?> type,
        PrivateKey signingKey
) {
}
