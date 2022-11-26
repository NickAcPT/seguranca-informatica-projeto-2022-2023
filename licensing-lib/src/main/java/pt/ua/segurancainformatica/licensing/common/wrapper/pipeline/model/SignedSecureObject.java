package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.model;

public record SignedSecureObject(byte[] object, byte[] signature) {
}
