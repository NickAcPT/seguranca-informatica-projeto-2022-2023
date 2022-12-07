package pt.ua.segurancainformatica.licensing.common.hashing;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HashingCommon {
    private HashingCommon() {
        throw new IllegalStateException("Utility class");
    }

    private static Path getCurrentJarPath() throws URISyntaxException {
        return new File(HashingCommon.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
    }

    public static String getCurrentJarHash() throws HashingException {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(Files.readAllBytes(getCurrentJarPath()));

            var hexString = new StringBuilder();
            for (var b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new HashingException("Could not hash current jar", e);
        }
    }
}
