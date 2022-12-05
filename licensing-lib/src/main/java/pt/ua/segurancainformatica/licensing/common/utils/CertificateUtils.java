package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertificateUtils {
    private CertificateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValid(@NotNull Certificate certificate) {
        if (!(certificate instanceof X509Certificate x509Certificate)) {
            return false;
        }

        return true;
    }
}
