package pt.ua.segurancainformatica.licensing.common.model.info.entries;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.info.SystemInformationEntry;

/**
 * Represents an environment variable entry.
 */
public record EnvironmentVariableEntry(@NotNull String key, @NotNull String value) implements SystemInformationEntry<String> {
    /**
     * Creates a new system information entry from an environment variable.
     *
     * @param key the name of the entry
     */
    public EnvironmentVariableEntry(String key) {
        this(key, System.getenv(key));
    }

    /**
     * Compares the information stored in this entry with the information computed from data in
     * this computer.
     *
     * @return {@code true} if the information stored in this entry is the same as the
     * information computed from data in this computer, {@code false} otherwise.
     */
    @Override
    public boolean matches() {
        return value.equals(System.getenv(key));
    }
}
