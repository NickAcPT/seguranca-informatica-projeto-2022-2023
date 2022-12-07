package pt.ua.segurancainformatica.licensing.common.model.info.entries;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.info.SystemInformationEntry;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Represents a network interface entry.
 */
public record NetworkInterfaceEntry(@NotNull String adapterName, @NotNull String friendlyName,
                                    byte @NotNull [] hardwareAddress) implements SystemInformationEntry<String, byte[]> {
    @Override
    public boolean matches() {
        try {
            var adapter = NetworkInterface.getByName(adapterName);
            return adapter != null && adapter.getHardwareAddress() != null && Arrays.equals(adapter.getHardwareAddress(), hardwareAddress);
        } catch (SocketException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "NetworkInterfaceEntry{" +
                "adapterName='" + adapterName + '\'' +
                ", friendlyName='" + friendlyName + '\'' +
                ", hardwareAddress=" + Arrays.toString(hardwareAddress) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkInterfaceEntry that)) return false;

        if (!adapterName.equals(that.adapterName)) return false;
        if (!friendlyName.equals(that.friendlyName)) return false;
        return Arrays.equals(hardwareAddress, that.hardwareAddress);
    }

    @Override
    public int hashCode() {
        int result = adapterName.hashCode();
        result = 31 * result + friendlyName.hashCode();
        result = 31 * result + Arrays.hashCode(hardwareAddress);
        return result;
    }
}
