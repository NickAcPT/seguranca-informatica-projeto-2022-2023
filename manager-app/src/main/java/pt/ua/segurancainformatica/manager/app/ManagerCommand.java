package pt.ua.segurancainformatica.manager.app;

import picocli.CommandLine;
import pt.ua.segurancainformatica.manager.lib.LicensingManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@CommandLine.Command(name = "manager", mixinStandardHelpOptions = true)
public class ManagerCommand {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ManagerCommand()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "generate")
    public Integer generateKeyPair() throws NoSuchAlgorithmException, IOException {
        if (LicensingManager.isKeyPairGenerated()) {
            System.out.println("Key pair already generated");
            return 1;
        }

        System.out.println("Generating key pair...");
        LicensingManager.generateKeyPair();
        System.out.println("Key pair generated");
        return 0;
    }

    @CommandLine.Command(name = "clear")
    public Integer clearKeyPair() {
        if (!LicensingManager.isKeyPairGenerated()) {
            System.out.println("Key pair not generated");
            return 1;
        }

        System.out.println("Clearing key pair...");
        LicensingManager.clearKeyPair();
        System.out.println("Key pair cleared");
        return 0;
    }
}
