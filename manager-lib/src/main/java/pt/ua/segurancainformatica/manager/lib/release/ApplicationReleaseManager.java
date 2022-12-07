package pt.ua.segurancainformatica.manager.lib.release;

import pt.ua.segurancainformatica.manager.lib.ManagerLicensingConstants;
import pt.ua.segurancainformatica.manager.lib.SimpleListStorageManager;

public class ApplicationReleaseManager extends SimpleListStorageManager<ApplicationRelease> {
    public static final ApplicationReleaseManager INSTANCE = new ApplicationReleaseManager();

    private ApplicationReleaseManager() {
        super(ManagerLicensingConstants.RELEASES_DATABASE_PATH, ApplicationRelease.class);
    }

}
