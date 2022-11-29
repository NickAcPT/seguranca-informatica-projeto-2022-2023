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

public class CipherSecureObjectStep implements SecureWrapperPipelineStep<CipherUtils.CipherResult, CipheredSecureObject> {
    @Override
    public CipheredSecureObject wrap(SecureWrapperPipelineContext<?> context, CipherUtils.CipherResult input) throws SecureWrapperInvalidatedException {
        var managerPubKey = context.managerKeyPair().getPublic();
        try {
            SecretKey cipherKey = context.cipherKey();
            if (cipherKey == null) {
                throw new SecureWrapperInvalidatedException("Cipher key is null.");
            }

            var cipheredSymmetric = CipherUtils.cipherBlob(
                    cipherKey.getEncoded(),
                    managerPubKey
            );

            if (cipheredSymmetric.iv() != null) {
                throw new SecureWrapperInvalidatedException("Symmetric key ciphered with an IV.");
            }

            return new CipheredSecureObject(input, cipheredSymmetric.blob());
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to cipher the symmetric key.", e);
        }
    }

    @Override
    public CipherUtils.CipherResult unwrap(SecureWrapperPipelineContext<?> context, CipheredSecureObject input) throws SecureWrapperInvalidatedException {
        PrivateKey managerPrivateKey = context.managerKeyPair().getPrivate();
        if (managerPrivateKey == null) {
            throw new SecureWrapperInvalidatedException("Unable to decipher the symmetric key because the manager private key is missing.");
        }

        try {
            var decipheredSymmetric = CipherUtils.decipherBlob(
                    new CipherUtils.CipherResult(input.key(), null),
                    managerPrivateKey
            );

            context.cipherKey(new SecretKeySpec(decipheredSymmetric, "AES"));

            return input.ciphered();
        } catch (Exception e) {
            throw new SecureWrapperInvalidatedException("Unable to decipher the symmetric key.", e);
        }
    }
}
