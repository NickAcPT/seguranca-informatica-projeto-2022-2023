package pt.ua.segurancainformatica.licensing.common.wrapper;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.ObjectCipherStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SignatureWrapperStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SmileMappingWrapperStep;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
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
        // Input: T, Output: byte[]
        steps.add(new SmileMappingWrapperStep());

        // Then, sign the value with the private key (to ensure integrity)
        // Input: byte[], Output: SignedSecureObject
        steps.add(new SignatureWrapperStep());

        // We re-convert to Smile Format, but now from a SignedSecureObject
        // Input: SignedSecureObject, Output: byte[]
        steps.add(new SmileMappingWrapperStep(SignedSecureObject.class));

        // Then, cipher the value with the cipher key
        // Input: byte[], Output: CipherUtils.CipherResult
        steps.add(new ObjectCipherStep());

        // Re-convert again to Smile Format, but now from a CipherUtils.CipherResult
        // Input: CipherUtils.CipherResult, Output: byte[]
        steps.add(new SmileMappingWrapperStep(CipherUtils.CipherResult.class));
    }

    private SecureWrapper() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("unchecked")
    private static <I, O> O performSteps(I originalInput, SecureWrapperPipelineContext ctx, Function<SecureWrapperPipelineStep<? super Object, ? super Object>, BiFunction<SecureWrapperPipelineContext, ? super Object, ? super Object>> action, boolean reverse) {
        Object tmpInput = originalInput;
        var stepsToPerform = new ArrayList<>(steps);

        if (reverse) Collections.reverse(stepsToPerform);

        for (SecureWrapperPipelineStep<?, ?> step : stepsToPerform) {
            var castStep = (SecureWrapperPipelineStep<? super Object, ? super Object>) step;
            var resultingStepFunction = (BiFunction<SecureWrapperPipelineContext, ? super Object, ? super Object>) action.apply(castStep);
            try {
                tmpInput = resultingStepFunction.apply(ctx, tmpInput);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return (O) tmpInput;
    }

    public static <T> byte[] wrapObject(T object, @NotNull SecureWrapperPipelineContext context) {
        return performSteps(object, context, (step) -> step::wrap, false);
    }

    public static <T> T unwrapObject(byte[] bytes, @NotNull SecureWrapperPipelineContext context) {
        return performSteps(bytes, context, (step) -> step::unwrap, true);
    }

    @NotNull
    public static <T> SecureWrapperPipelineContext createContext(Class<T> type, PrivateKey signingKey, SecretKey cipherKey) {
        return new SecureWrapperPipelineContext(type, signingKey, cipherKey);
    }
}
