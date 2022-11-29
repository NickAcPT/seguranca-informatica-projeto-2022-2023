package pt.ua.segurancainformatica.licensing.common.model.license;

import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.ComputerInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;

public record LicenseInformation(
        UserData user,
        ApplicationInformation application,
        ComputerInformation computer,
        LicenseData license
) {
    public boolean isValid() {
        return false;
    }
}
