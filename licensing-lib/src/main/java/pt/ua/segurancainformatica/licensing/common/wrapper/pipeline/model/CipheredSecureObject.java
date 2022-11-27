package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model;

import pt.ua.segurancainformatica.licensing.common.utils.CipherUtils;

public record CipheredSecureObject(CipherUtils.CipherResult ciphered, byte[] key) {
}
