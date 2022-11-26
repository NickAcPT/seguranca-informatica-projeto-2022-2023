package pt.ua.segurancainformatica.licensing.common.model.request;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.LicensingCommon;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.EnvironmentVariableEntry;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.NetworkInterfaceEntry;

import java.util.Arrays;

public record LicenseRequest(
        @NotNull UserData user,
        @NotNull ApplicationInformation application,
        @NotNull EnvironmentVariableEntry[] environmentVariables,
        @NotNull NetworkInterfaceEntry[] networkInterfaces
) {
    public LicenseRequest(UserData user, ApplicationInformation application) {
        this(user, application, LicensingCommon.getEnvironmentVariableEntries(), LicensingCommon.getNetworkInterfaceEntries());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseRequest that)) return false;

        if (!user.equals(that.user)) return false;
        if (!application.equals(that.application)) return false;
        if (!Arrays.equals(environmentVariables, that.environmentVariables)) return false;
        return Arrays.equals(networkInterfaces, that.networkInterfaces);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + application.hashCode();
        result = 31 * result + Arrays.hashCode(environmentVariables);
        result = 31 * result + Arrays.hashCode(networkInterfaces);
        return result;
    }

    @Override
    public String toString() {
        return "LicenseRequest{" +
                "user=" + user +
                ", application=" + application +
                ", environmentVariables=" + Arrays.toString(environmentVariables) +
                ", networkInterfaces=" + Arrays.toString(networkInterfaces) +
                '}';
    }
}
