package pt.ua.segurancainformatica.licensing.common.model;

import org.jetbrains.annotations.NotNull;

public record ApplicationInformation(
        @NotNull String name,
        @NotNull String version,
        @NotNull String hash
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationInformation that)) return false;

        if (!name.equals(that.name)) return false;
        if (!version.equals(that.version)) return false;
        return hash.equals(that.hash);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + hash.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ApplicationInformation{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
