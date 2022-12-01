package pt.ua.segurancainformatica.licensing.lib;

import java.nio.file.Path;

public class LicensingConstants {
    private static final String LICENSE_FILE_NAME = "license.lic";
    public static final Path LICENSE_FILE_PATH = Path.of(LICENSE_FILE_NAME);
    private static final String LICENSE_REQUEST_FILE_NAME = "license.req";
    public static final Path LICENSE_REQUEST_PATH = Path.of(LICENSE_REQUEST_FILE_NAME);

    private LicensingConstants() {
        throw new IllegalStateException("Utility class");
    }
}
