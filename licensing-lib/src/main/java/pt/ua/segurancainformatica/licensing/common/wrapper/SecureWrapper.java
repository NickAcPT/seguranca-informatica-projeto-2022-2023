package pt.ua.segurancainformatica.licensing.common.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineSide;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.CipheredSecureObject;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.CipherSecureObjectStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.ObjectCipherStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SignatureWrapperStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps.SmileMappingWrapperStep;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * A helper class used to wrap and unwrap objects securely, step by step.
 */
@SuppressWarnings("TypeParameterUnusedInFormals")
public class SecureWrapper {

    @FunctionalInterface
    private interface BiFunctionThrowingException<T, U, R> {
        R apply(T t, U u) throws SecureWrapperInvalidatedException;
    }

    private static final List<SecureWrapperPipelineStep<?, ?>> steps = new ArrayList<>();

    static {
        // First, convert to Smile Format (binary JSON)
        // Input: T, Output: byte[]
        steps.add(new SmileMappingWrapperStep());

        // Then, sign the value with the user's private key (to ensure integrity)
        // Input: byte[], Output: SignedSecureObject
        steps.add(new SignatureWrapperStep());

        // We re-convert to Smile Format, but now from a SignedSecureObject
        // Input: SignedSecureObject, Output: byte[]
        steps.add(new SmileMappingWrapperStep(SignedSecureObject.class));

        // Then, cipher the value with the cipher key
        // Input: byte[], Output: CipherUtils.CipherResult
        steps.add(new ObjectCipherStep());

        // Then, we need to include our symmetric key, ciphered with the manager's public key
        // Input: CipherUtils.CipherResult, Output: CipheredSecureObject
        steps.add(new CipherSecureObjectStep());

        // Re-convert again to Smile Format, but now from a CipheredSecureObject
        // Input: CipheredSecureObject, Output: byte[]
        steps.add(new SmileMappingWrapperStep(CipheredSecureObject.class));
    }

    private SecureWrapper() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("unchecked")
    private static <I, O> O performSteps(I originalInput, SecureWrapperPipelineContext<?> ctx, Function<SecureWrapperPipelineStep<? super Object, ? super Object>, BiFunctionThrowingException<SecureWrapperPipelineContext<?>, ? super Object, ? super Object>> action, boolean reverse) throws SecureWrapperInvalidatedException {
        Object tmpInput = originalInput;
        var stepsToPerform = new ArrayList<>(steps);

        if (reverse) Collections.reverse(stepsToPerform);

        for (SecureWrapperPipelineStep<?, ?> step : stepsToPerform) {
            var castStep = (SecureWrapperPipelineStep<? super Object, ? super Object>) step;
            var resultingStepFunction = (BiFunctionThrowingException<SecureWrapperPipelineContext<?>, ? super Object, ? super Object>) action.apply(castStep);
            tmpInput = resultingStepFunction.apply(ctx, tmpInput);
        }

        return (O) tmpInput;
    }

    public static <T> byte[] wrapObject(T object, @NotNull SecureWrapperPipelineContext<T> context) throws SecureWrapperInvalidatedException {
        return performSteps(object, context, (step) -> step::wrap, false);
    }

    public static <T> T unwrapObject(byte[] bytes, @NotNull SecureWrapperPipelineContext<T> context) throws SecureWrapperInvalidatedException {
        return performSteps(bytes, context, (step) -> step::unwrap, true);
    }

    @NotNull
    public static <T> SecureWrapperPipelineContext<T> createContext(Class<T> type, KeyPair managerPublicKey, @Nullable KeyPair userKeyPair, @Nullable SecretKey cipherKey, SecureWrapperPipelineSide side) {
        return new SecureWrapperPipelineContext<>(type, side, managerPublicKey, userKeyPair, cipherKey);
    }
}
