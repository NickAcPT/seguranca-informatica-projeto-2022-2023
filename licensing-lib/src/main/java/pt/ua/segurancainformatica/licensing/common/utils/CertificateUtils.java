package pt.ua.segurancainformatica.licensing.common.utils;

import org.jetbrains.annotations.NotNull;

import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class CertificateUtils {
    private CertificateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValid(@NotNull Certificate certificateChain) {
        if (certificateChain instanceof X509Certificate x509Certificate) {
            try {
                x509Certificate.checkValidity();
                return true;
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                return false;
            }
        }
        return false;
    }
}
