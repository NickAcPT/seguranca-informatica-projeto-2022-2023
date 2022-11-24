package pt.ua.segurancainformatica.licensing.common;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.info.EnvironmentVariableEntry;

import java.util.List;

public class LicensingCommon {
    private static final List<String> ENVIRONMENT_VARIABLE_ENTRY_KEYS = List.of(
            "USERNAME", // User name
            "OS", // Operating system
            "COMPUTERNAME", // Computer name
            "PROCESSOR_IDENTIFIER", // Processor identifier
            "PROCESSOR_ARCHITECTURE", // Processor architecture
            "NUMBER_OF_PROCESSORS" // Processor count
    );

    private LicensingCommon() {
        throw new IllegalStateException("Utility class");
    }

    public static @NotNull EnvironmentVariableEntry[] getEnvironmentVariableEntries() {
        return ENVIRONMENT_VARIABLE_ENTRY_KEYS.stream()
                .map(EnvironmentVariableEntry::new)
                .toArray(EnvironmentVariableEntry[]::new);
    }
}
