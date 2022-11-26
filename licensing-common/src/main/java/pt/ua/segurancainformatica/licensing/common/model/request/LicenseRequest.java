package pt.ua.segurancainformatica.licensing.common.model.request;

import pt.ua.segurancainformatica.licensing.common.LicensingCommon;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.EnvironmentVariableEntry;
import pt.ua.segurancainformatica.licensing.common.model.info.entries.NetworkInterfaceEntry;

public record LicenseRequest(
        UserData user,
        ApplicationInformation application,
        EnvironmentVariableEntry[] environmentVariables,
        NetworkInterfaceEntry[] networkInterfaces
) {
    public LicenseRequest(UserData user, ApplicationInformation application) {
        this(user, application, LicensingCommon.getEnvironmentVariableEntries(), LicensingCommon.getNetworkInterfaceEntries());
    }
}
