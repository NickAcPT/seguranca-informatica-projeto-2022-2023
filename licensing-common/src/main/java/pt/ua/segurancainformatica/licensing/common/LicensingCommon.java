package pt.ua.segurancainformatica.licensing.common;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.EnvironmentVariableEntry;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.NetworkInterfaceEntry;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
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

    private static final List<String> FILTERED_NETWORK_INTERFACE_NAMES = List.of(
            "Virtual Adapter",
            "Hyper-V",
            "Bluetooth",
            "VMware",
            "VirtualBox",
            "TAP",
            "TUN",
            "Docker"
    );

    private LicensingCommon() {
        throw new IllegalStateException("Utility class");
    }

    public static @NotNull EnvironmentVariableEntry[] getEnvironmentVariableEntries() {
        return ENVIRONMENT_VARIABLE_ENTRY_KEYS.stream()
                .map(EnvironmentVariableEntry::new)
                .toArray(EnvironmentVariableEntry[]::new);
    }

    public static @NotNull NetworkInterfaceEntry[] getNetworkInterfaceEntries() {
        List<NetworkInterfaceEntry> entries = new ArrayList<>();

        try {
            var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                var i = interfaces.nextElement();
                var hardwareAddress = i.getHardwareAddress();
                if (hardwareAddress != null && FILTERED_NETWORK_INTERFACE_NAMES.stream().noneMatch(i.getDisplayName()::contains)) {
                    entries.add(new NetworkInterfaceEntry(i.getDisplayName(), hardwareAddress));
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return new NetworkInterfaceEntry[0];
        }

        return entries.toArray(new NetworkInterfaceEntry[0]);
    }
}
