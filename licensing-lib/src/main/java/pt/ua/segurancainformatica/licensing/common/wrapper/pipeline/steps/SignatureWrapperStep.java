package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.KeyUtils;
import pt.ua.segurancainformatica.licensing.common.utils.SignatureUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Arrays;

public class SignatureWrapperStep implements SecureWrapperPipelineStep<byte[], SignedSecureObject> {
    @Override
    public SignedSecureObject wrap(SecureWrapperPipelineContext<?> context, byte[] input) throws SecureWrapperInvalidatedException {
        byte[] signature;

        if (context.userKeyPair() == null) {
            throw new SecureWrapperInvalidatedException("User key pair is null.");
        }

        try {
            signature = SignatureUtils.signBlob(context.userKeyPair().getPrivate(), input);
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to sign the object.", e);
        }

        if (signature == null) throw new SecureWrapperInvalidatedException("Object signature returned null.");

        return new SignedSecureObject(input, signature, context.userKeyPair().getPublic().getEncoded());
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext<?> context, SignedSecureObject input) throws SecureWrapperInvalidatedException {
        try {
            if (!SignatureUtils.verifyBlob(input.publicKey(), input.object(), input.signature())) {
                throw new SecureWrapperInvalidatedException("Object signature is invalid.");
            }
            if (context.userKeyPair() != null && context.userKeyPair().getPublic() != null && Arrays.equals(
                    context.userKeyPair().getPublic().getEncoded(), input.publicKey())) {
                throw new SecureWrapperInvalidatedException(
                        "Known public key is different from the one used to sign the object.");
            }

            // Initialize the user key pair with the public key used to sign the object.
            if (context.userKeyPair() == null) context.userKeyPair(new KeyPair(null, null));
            context.userKeyPair(new KeyPair(
                    KeyUtils.getPublicKeyFromBytes(input.publicKey()),
                    context.userKeyPair() != null ? context.userKeyPair().getPrivate() : null
            ));
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException("Unable to verify the object signature.", e);
        }
        return input.object();
    }
}
