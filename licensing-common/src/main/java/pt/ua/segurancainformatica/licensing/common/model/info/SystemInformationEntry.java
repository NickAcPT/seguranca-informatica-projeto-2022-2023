package pt.ua.segurancainformatica.licensing.common.model.info;

/**
 * Represents a system information entry.
 * <p>
 * A license gets generated for a specific system, and then the system information is used to verify if the
 * license is valid for the system.
 */
public interface SystemInformationEntry<T> {
    /**
     * Whether the information stored in this entry matches the information computed from data in this computer.
     * @return {@code true} if the information stored in this entry is the same as the information computed from
     * data in this computer, {@code false} otherwise.
     */
    boolean matches();

    /**
     * The name of the entry.
     * @return
     */
    String key();

    /**
     * The value of the entry.
     * @return the value of the entry.
     */
    T value();
}
