package pt.ua.segurancainformatica.manager.lib.licenses;

import pt.ua.segurancainformatica.licensing.common.model.license.LicenseInformation;
import pt.ua.segurancainformatica.manager.lib.ManagerLicensingConstants;
import pt.ua.segurancainformatica.manager.lib.SimpleListStorageManager;

public class LicenseInformationManager extends SimpleListStorageManager<LicenseInformation> {
    public static final LicenseInformationManager INSTANCE = new LicenseInformationManager();

    private LicenseInformationManager() {
        super(ManagerLicensingConstants.LICENSES_DATABASE_PATH, LicenseInformation.class);
    }
}
