package pt.ua.segurancainformatica.manager.app;

import picocli.CommandLine;
import pt.ua.segurancainformatica.licensing.common.model.license.LicenseInformation;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.manager.lib.LicensingManager;
import pt.ua.segurancainformatica.manager.lib.LicensingManagerException;
import pt.ua.segurancainformatica.manager.lib.licenses.LicenseInformationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "license")

public class LicenseCommands {
    private static void printLicense(int i, LicenseInformation license) {
        var application = license.application();
        var user = license.user();

        System.out.printf("%d. License for application \"%s\" version \"%s\" (hash: %s):%n", i + 1, application.name(), application.version(), application.hash());
        System.out.printf("   - Licensee: \"%s\" (civil id: %s)%n", user.fullName(), user.civilNumber());
        System.out.printf("   - Start date: %s%n", license.license().startDate());
        System.out.printf("   - Expiration date: %s%n", license.license().endDate());
    }

    @CommandLine.Command(name = "list", description = "List all application releases.")
    public Integer listReleases() {
        var licenses = LicenseInformationManager.INSTANCE.getValues();

        System.out.println("Licensed Users:");
        if (licenses.isEmpty()) {
            System.out.println("No application licenses found");
            return 1;
        } else {
            for (int i = 0; i < licenses.size(); i++) {
                LicenseInformation license = licenses.get(i);
                printLicense(i, license);
            }
        }
        return 0;
    }

    @CommandLine.Command(name = "process", description = "Process a license request")
    public Integer processRequest(
            @CommandLine.Parameters(index = "0", description = "The license request") Path licenseRequest
    ) {
        if (!Files.exists(licenseRequest)) {
            System.out.println("The license request does not exist");
            return 1;
        }

        try {
            var reqBytes = Files.readAllBytes(licenseRequest);

            LicensingManager.processRequest(reqBytes);
        } catch (IOException | SecureWrapperInvalidatedException | LicensingManagerException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
