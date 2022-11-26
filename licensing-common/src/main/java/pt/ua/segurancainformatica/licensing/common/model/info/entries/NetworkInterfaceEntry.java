package pt.ua.segurancainformatica.licensing.common.model.info.entries;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.info.SystemInformationEntry;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Represents a network interface entry.
 */
public record NetworkInterfaceEntry(@NotNull String adapterName,
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
    public String key() {
        return adapterName;
    }

    @Override
    public byte[] value() {
        return hardwareAddress;
    }

    @Override
    public String toString() {
        return "NetworkInterfaceEntry{" +
                "adapterName='" + adapterName + '\'' +
                ", hardwareAddress=" + Arrays.toString(hardwareAddress) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkInterfaceEntry that = (NetworkInterfaceEntry) o;

        if (!adapterName.equals(that.adapterName)) return false;
        return Arrays.equals(hardwareAddress, that.hardwareAddress);
    }

    @Override
    public int hashCode() {
        int result = adapterName.hashCode();
        result = 31 * result + Arrays.hashCode(hardwareAddress);
        return result;
    }
}
