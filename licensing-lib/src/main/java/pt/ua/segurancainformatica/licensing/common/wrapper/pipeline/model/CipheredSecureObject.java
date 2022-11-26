package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model;

public record CipheredSecureObject(byte[] ciphered, byte[] iv, byte[] key) {
}
