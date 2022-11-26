package pt.ua.segurancainformatica.licensing.common.wrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SmileMappingWrapperStep;

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
        steps.add(new SmileMappingWrapperStep());
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

    public static <T> byte[] wrapObject(T object) {
        return performSteps(object, SecureWrapper.<T>createContext(), (step) -> step::wrap);
    }

    public static <T> T unwrapObject(byte[] bytes) {
        return performSteps(bytes, SecureWrapper.<T>createContext(), (step) -> step::unwrap);
    }

    @NotNull
    private static <T> SecureWrapperPipelineContext createContext() {
        TypeReference<T> type = new TypeReference<>() {
        };

        return new SecureWrapperPipelineContext(type);
    }
}
