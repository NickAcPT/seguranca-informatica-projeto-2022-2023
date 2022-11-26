package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;

import java.security.GeneralSecurityException;

public class ObjectCipherStep implements SecureWrapperPipelineStep<byte[], CipherUtils.CipherResult> {
    @Override
    public CipherUtils.CipherResult wrap(SecureWrapperPipelineContext context, byte[] input) throws SecureWrapperInvalidatedException {
        try {
            return CipherUtils.cipherBlob(input, context.cipherKey());
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to cipher the object.", e);
        }
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        try {
            return CipherUtils.decipherBlob(input, context.cipherKey());
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to decipher the object.", e);
        }
    }
}
