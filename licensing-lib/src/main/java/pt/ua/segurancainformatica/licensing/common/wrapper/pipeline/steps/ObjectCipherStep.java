package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;

import javax.crypto.SecretKey;

import static pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedReason.*;

public class ObjectCipherStep implements SecureWrapperPipelineStep<byte[], CipherUtils.CipherResult> {
    @Override
    public CipherUtils.CipherResult wrap(SecureWrapperPipelineContext<?> context, byte[] input) throws SecureWrapperInvalidatedException {
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) throw new SecureWrapperInvalidatedException(NULL_CIPHER_KEY);

            return CipherUtils.cipherBlob(input, cipherKey);
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException(ERROR_CIPHERING_SECURE_OBJECT, e);
        }
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext<?> context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) throw new SecureWrapperInvalidatedException(NULL_CIPHER_KEY);

            return CipherUtils.decipherBlob(input, cipherKey);
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException(ERROR_DECIPHERING_SECURE_OBJECT, e);
        }
    }
}
