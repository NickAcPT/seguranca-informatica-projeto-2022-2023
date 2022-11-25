package pt.ua.segurancainformatica.licensing.common.model.request;

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
}
