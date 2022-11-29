package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;

import javax.crypto.SecretKey;

public class ObjectCipherStep implements SecureWrapperPipelineStep<byte[], CipherUtils.CipherResult> {
    @Override
    public CipherUtils.CipherResult wrap(SecureWrapperPipelineContext<?> context, byte[] input) throws SecureWrapperInvalidatedException {
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) throw new SecureWrapperInvalidatedException("Cipher key is null.");

            return CipherUtils.cipherBlob(input, cipherKey);
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException("Unable to cipher the object.", e);
        }
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext<?> context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) throw new SecureWrapperInvalidatedException("Cipher key is null.");

            return CipherUtils.decipherBlob(input, cipherKey);
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException("Unable to decipher the object.", e);
        }
    }
}
