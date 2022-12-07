package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineSide;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.CipheredSecureObject;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Objects;

import static pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedReason.*;

public class CipherSecureObjectStep implements SecureWrapperPipelineStep<CipherUtils.CipherResult, CipheredSecureObject> {
    @Override
    public CipheredSecureObject wrap(SecureWrapperPipelineContext<?> context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        // Cifrar no utilizador - cifrar com a chave pública do gestor
        // Cifrar no gestor - cifrar com a chave privada do gestor
        var keyToUse = context.side() == SecureWrapperPipelineSide.USER ? context.managerKeyPair().getPublic() : context.managerKeyPair().getPrivate();
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) {
                throw new SecureWrapperInvalidatedException(NULL_CIPHER_KEY);
            }

            var cipheredSymmetric = CipherUtils.cipherBlob(
                    cipherKey.getEncoded(),
                    keyToUse
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

        // Descifrar no utilizador - descifrar com a chave publica do gestor
        // Descifrar no gestor - descifrar com a chave privada do gestor
        var keyToUse = context.side() != SecureWrapperPipelineSide.USER ? context.managerKeyPair().getPrivate() : context.managerKeyPair().getPublic();

        if (keyToUse == null) {
            throw new SecureWrapperInvalidatedException(PRIVATE_KEY_MISSING);
        }

        try {
            var decipheredSymmetric = CipherUtils.decipherBlob(
                    new CipherUtils.CipherResult(input.key(), null),
                    keyToUse
            );

            context.cipherKey(new SecretKeySpec(decipheredSymmetric, "AES"));

            return input.ciphered();
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException(ERROR_DECIPHERING_SYMMETRIC_KEY, e);
        }
    }
}
