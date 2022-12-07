package pt.ua.segurancainformatica.manager.lib.release;

public record ApplicationRelease(
        String name,
        String version,
        String hash
) {

    @Override
    public String toString() {
        return String.format("Application \"%s\" version \"%s\" (\"%s\")", name, version, hash);
    }
}
