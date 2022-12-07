package pt.ua.segurancainformatica.manager.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ManagerLicensingConstants {

    public static final String LICENSE_TEMPLATE_NAME = "license-%name%.lic";
    private static final String RELEASES_DATABASE_NAME = "releases.bin";
    private static final String LICENSES_DATABASE_NAME = "licenses.bin";
    private static final String HOME_DIRECTORY_NAME = ".segurancainformatica";
    private static final Path BASE_PATH = Paths.get(System.getProperty("user.home"), HOME_DIRECTORY_NAME);
    public static final Path RELEASES_DATABASE_PATH = BASE_PATH.resolve(RELEASES_DATABASE_NAME);
    public static final Path LICENSES_DATABASE_PATH = BASE_PATH.resolve(LICENSES_DATABASE_NAME);
    private static final String PUBLIC_KEY_NAME = "manager-public.key";
    public static final Path PUBLIC_KEY_PATH = BASE_PATH.resolve(PUBLIC_KEY_NAME);
    private static final String PRIVATE_KEY_NAME = "manager-private.key";
    public static final Path PRIVATE_KEY_PATH = BASE_PATH.resolve(PRIVATE_KEY_NAME);

    static {
        try {
            Files.createDirectories(BASE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create ~/.segurancainformatica", e);
        }
    }

    private ManagerLicensingConstants() {
        throw new IllegalStateException("Utility class");
    }
}
