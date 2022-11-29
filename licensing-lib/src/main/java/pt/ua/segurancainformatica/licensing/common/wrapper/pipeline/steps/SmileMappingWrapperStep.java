package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;

public class SmileMappingWrapperStep implements SecureWrapperPipelineStep<Object, byte[]> {
    private final SmileMapper mapper = new SmileMapper();
    private @Nullable Class<?> expectedDeserializeType = null;

    public SmileMappingWrapperStep() {
    }

    public SmileMappingWrapperStep(@NotNull Class<?> expectedDeserializeType) {
        this.expectedDeserializeType = expectedDeserializeType;
    }

    @Override
    public byte[] wrap(SecureWrapperPipelineContext<?> context, Object input) throws SecureWrapperInvalidatedException {
        try {
            return mapper.writeValueAsBytes(input);
        } catch (JsonProcessingException e) {
            throw new SecureWrapperInvalidatedException("Unable to serialize the object to smile format.", e);
        }
    }

    @Override
    public Object unwrap(SecureWrapperPipelineContext<?> context, byte[] input) throws SecureWrapperInvalidatedException {
        try {
            if (expectedDeserializeType != null) {
                return mapper.readValue(input, expectedDeserializeType);
            } else {
                return mapper.readValue(input, context.type());
            }
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException("Unable to serialize the object to smile format.", e);
        }
    }
}
