package pt.ua.segurancainformatica.licensing.common.model;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.LicensingCommon;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.EnvironmentVariableEntry;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.NetworkInterfaceEntry;

import java.util.Arrays;

public record ComputerInformation(
        @NotNull EnvironmentVariableEntry[] environmentVariables,
        @NotNull NetworkInterfaceEntry[] networkInterfaces
) {

    public ComputerInformation() {
        this(LicensingCommon.getEnvironmentVariableEntries(), LicensingCommon.getNetworkInterfaceEntries());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComputerInformation that = (ComputerInformation) o;

        if (!Arrays.equals(environmentVariables, that.environmentVariables)) return false;
        return Arrays.equals(networkInterfaces, that.networkInterfaces);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(environmentVariables);
        result = 31 * result + Arrays.hashCode(networkInterfaces);
        return result;
    }

    @Override
    public String toString() {
        return "ComputerInformation{" +
                "environmentVariables=" + Arrays.toString(environmentVariables) +
                ", networkInterfaces=" + Arrays.toString(networkInterfaces) +
                '}';
    }
}
