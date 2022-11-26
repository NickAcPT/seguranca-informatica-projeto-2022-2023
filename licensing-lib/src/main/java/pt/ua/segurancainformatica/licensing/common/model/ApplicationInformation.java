package pt.ua.segurancainformatica.licensing.common.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record ApplicationInformation(
        @NotNull String name,
        @NotNull String version,
        byte @NotNull [] hash
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationInformation that = (ApplicationInformation) o;

        if (!name.equals(that.name)) return false;
        if (!version.equals(that.version)) return false;
        return Arrays.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + Arrays.hashCode(hash);
        return result;
    }
}
