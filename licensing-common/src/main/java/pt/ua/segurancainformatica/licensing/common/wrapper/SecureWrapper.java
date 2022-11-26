package pt.ua.segurancainformatica.licensing.common.wrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SignatureWrapperStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SmileMappingWrapperStep;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A helper class used to wrap and unwrap objects securely, step by step.
 */
@SuppressWarnings("TypeParameterUnusedInFormals")
public class SecureWrapper {

    private static final List<SecureWrapperPipelineStep<?, ?>> steps = new ArrayList<>();

    static {
        // First, convert to Smile Format (binary JSON)
        steps.add(new SmileMappingWrapperStep());
        // Then, sign the value with the private key (to ensure integrity)
        steps.add(new SignatureWrapperStep());
        // We re-convert to Smile Format, but now from a SignedSecureObject
        steps.add(new SmileMappingWrapperStep(SignedSecureObject.class));
    }

    private SecureWrapper() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("unchecked")
    private static <I, O> O performSteps(I originalInput, SecureWrapperPipelineContext ctx, Function<SecureWrapperPipelineStep<? super Object, ? super Object>, BiFunction<SecureWrapperPipelineContext, ? super Object, ? super Object>> action) {
        Object tmpInput = originalInput;
        for (SecureWrapperPipelineStep<?, ?> step : steps) {
            var castStep = (SecureWrapperPipelineStep<? super Object, ? super Object>) step;
            var resultingStepFunction = (BiFunction<SecureWrapperPipelineContext, ? super Object, ? super Object>) action.apply(castStep);
            tmpInput = resultingStepFunction.apply(ctx, tmpInput);
        }

        return (O) tmpInput;
    }

    public static <T> byte[] wrapObject(T object, @NotNull SecureWrapperPipelineContext context) {
        return performSteps(object, context, (step) -> step::wrap);
    }

    public static <T> T unwrapObject(byte[] bytes, @NotNull SecureWrapperPipelineContext context) {
        return performSteps(bytes, context, (step) -> step::unwrap);
    }

    @NotNull
    private static <T> SecureWrapperPipelineContext createContext(PrivateKey signingKey) {
        TypeReference<T> type = new TypeReference<>() {
        };

        return new SecureWrapperPipelineContext(type, signingKey);
    }
}
