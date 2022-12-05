package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.CipheredSecureObject;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import static pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedReason.*;

public class CipherSecureObjectStep implements SecureWrapperPipelineStep<CipherUtils.CipherResult, CipheredSecureObject> {
    @Override
    public CipheredSecureObject wrap(SecureWrapperPipelineContext<?> context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        var managerPubKey = context.managerKeyPair().getPublic();
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) {
                throw new SecureWrapperInvalidatedException(NULL_CIPHER_KEY);
            }

            var cipheredSymmetric = CipherUtils.cipherBlob(
                    cipherKey.getEncoded(),
                    managerPubKey
            );

            if (cipheredSymmetric.iv() != null) {
                throw new SecureWrapperInvalidatedException(SYMMETRIC_KEY_CIPHERED_WITH_IV);
            }

            return new CipheredSecureObject(input, cipheredSymmetric.blob());
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException(ERROR_CIPHERING_SYMMETRIC_KEY, e);
        }
    }

    @Override
    public CipherUtils.CipherResult unwrap(SecureWrapperPipelineContext<?> context, CipheredSecureObject input) throws SecureWrapperInvalidatedException {
        PrivateKey managerPrivateKey = context.managerKeyPair().getPrivate();
        if (managerPrivateKey == null) {
            throw new SecureWrapperInvalidatedException(MANAGER_PRIVATE_KEY_MISSING);
        }

        try {
            var decipheredSymmetric = CipherUtils.decipherBlob(
                    new CipherUtils.CipherResult(input.key(), null),
                    managerPrivateKey
            );

            context.cipherKey(new SecretKeySpec(decipheredSymmetric, "AES"));

            return input.ciphered();
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException(ERROR_DECIPHERING_SYMMETRIC_KEY, e);
        }
    }
}
