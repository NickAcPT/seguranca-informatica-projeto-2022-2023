package pt.ua.segurancainformatica.licensing.common.model;

public record ApplicationInformation(
        String name,
        String version,
        byte[] hash
) {
}
