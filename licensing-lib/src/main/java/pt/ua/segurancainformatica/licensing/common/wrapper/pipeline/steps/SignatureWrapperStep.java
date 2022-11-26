package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.SignatureUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;

import java.security.GeneralSecurityException;

public class SignatureWrapperStep implements SecureWrapperPipelineStep<byte[], SignedSecureObject> {
    @Override
    public SignedSecureObject wrap(SecureWrapperPipelineContext context, byte[] input) throws SecureWrapperInvalidatedException {
        byte[] signature;

        try {
            signature = SignatureUtils.signBlob(context.userKeyPair().getPrivate(), input);
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to sign the object.", e);
        }

        if (signature == null) throw new SecureWrapperInvalidatedException("Object signature returned null.");

        return new SignedSecureObject(input, signature);
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext context, SignedSecureObject input) throws SecureWrapperInvalidatedException {
        try {
            if (!SignatureUtils.verifyBlob(context.userKeyPair().getPublic(), input.object(), input.signature())) {
                throw new SecureWrapperInvalidatedException("Object signature is invalid.");
            }
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to verify the object signature.", e);
        }
        return input.object();
    }
}
