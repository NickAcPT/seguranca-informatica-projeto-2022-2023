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

import static pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedReason.*;

public class SignatureWrapperStep implements SecureWrapperPipelineStep<byte[], SignedSecureObject> {
    @Override
    public SignedSecureObject wrap(SecureWrapperPipelineContext<?> context, byte[] input) throws SecureWrapperInvalidatedException {
        byte[] signature;

        if (context.userKeyPair() == null) {
            throw new SecureWrapperInvalidatedException(USER_KEYPAIR_MISSING);
        }

        try {
            signature = SignatureUtils.signBlob(context.userKeyPair().getPrivate(), input);
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException(ERROR_SIGNING_SECURE_OBJECT, e);
        }

        if (signature == null) throw new SecureWrapperInvalidatedException(OBJECT_HAS_NO_SIGNATURE);

        return new SignedSecureObject(input, signature, context.userKeyPair().getPublic().getEncoded());
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext<?> context, SignedSecureObject input) throws SecureWrapperInvalidatedException {
        try {
            if (!SignatureUtils.verifyBlob(input.publicKey(), input.object(), input.signature())) {
                throw new SecureWrapperInvalidatedException(INVALID_OBJECT_SIGNATURE);
            }
            if (context.userKeyPair() != null && context.userKeyPair().getPublic() != null && Arrays.equals(
                    context.userKeyPair().getPublic().getEncoded(), input.publicKey())) {
                throw new SecureWrapperInvalidatedException(PUBLIC_KEY_MISMATCH);
            }

            // Initialize the user key pair with the public key used to sign the object.
            if (context.userKeyPair() == null) context.userKeyPair(new KeyPair(null, null));
            context.userKeyPair(new KeyPair(
                    KeyUtils.getPublicKeyFromBytes(input.publicKey()),
                    context.userKeyPair() != null ? context.userKeyPair().getPrivate() : null
            ));
        } catch (GeneralSecurityException e) {
            throw new SecureWrapperInvalidatedException(ERROR_VERIFYING_SIGNATURE, e);
        }
        return input.object();
    }
}
