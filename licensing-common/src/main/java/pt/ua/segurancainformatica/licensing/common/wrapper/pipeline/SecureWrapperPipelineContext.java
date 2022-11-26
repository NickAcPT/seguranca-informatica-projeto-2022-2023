package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

import com.fasterxml.jackson.core.type.TypeReference;

public record SecureWrapperPipelineContext(
        TypeReference<?> type
) {
}
