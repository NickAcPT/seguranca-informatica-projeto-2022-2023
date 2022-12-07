package pt.ua.segurancainformatica.manager.app;

import picocli.CommandLine;
import pt.ua.segurancainformatica.manager.lib.LicensingManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

@CommandLine.Command(name = "key")
public class KeyCommands {

    @CommandLine.Command(name = "generate", description = "Generate a new key pair for the application.")
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

    @CommandLine.Command(name = "clear", description = "Clear the key pair for the application.")
    public Integer clearKeyPair() {
        if (!LicensingManager.isKeyPairGenerated()) {
            System.out.println("Key pair not generated");
            return 1;
        }

        // Ask for confirmation
        System.out.println("Are you sure you want to clear the key pair? (y/n)");
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        var input = scanner.nextLine();
        if (!input.equals("y")) {
            System.out.println("Key pair not cleared");
            return 1;
        }

        System.out.println("Clearing key pair...");
        LicensingManager.clearKeyPair();
        System.out.println("Key pair cleared");
        return 0;
    }

}
