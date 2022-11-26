package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.steps;

import pt.ua.segurancainformatica.licensing.common.utils.SignatureUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineStep;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model.SignedSecureObject;

public class SignatureWrapperStep implements SecureWrapperPipelineStep<byte[], SignedSecureObject> {
    @Override
    public SignedSecureObject wrap(SecureWrapperPipelineContext context, byte[] input) {
        var signature = SignatureUtils.signBlob(context.signingKey(), input);
        if (signature == null) {
            throw new NullPointerException("Signature is null");
        }
        return new SignedSecureObject(input, signature);
    }

    @Override
    public byte[] unwrap(SecureWrapperPipelineContext context, SignedSecureObject input) {
        return input.object();
    }
}
