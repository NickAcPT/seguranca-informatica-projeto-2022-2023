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
                                    byte @NotNull [] hardwareAddress) implements SystemInformationEntry<byte[]> {
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
}
