package pt.ua.segurancainformatica.licensing.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

public record UserData(
        @NotNull String fullName,
        @NotNull String civilNumber,
        byte @NotNull [] encodedAuthenticationCertificate,
        boolean isAuthenticationCertificateValidAccordingToPteidSDK) {

    public UserData(@NotNull String fullName, @NotNull String civilNumber, @NotNull Certificate certificate, boolean isAuthenticationCertificateValidAccordingToPteidSDK) throws CertificateEncodingException {
        this(fullName, civilNumber, certificate.getEncoded(), isAuthenticationCertificateValidAccordingToPteidSDK);
    }

    @JsonIgnore
    public @Nullable Certificate authenticationCertificate() {
        try {
            var certificateFactory = CertificateFactory.getInstance("X.509");
            certificateFactory.generateCertificate(new ByteArrayInputStream(encodedAuthenticationCertificate));
        } catch (CertificateException e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData userData)) return false;

        if (isAuthenticationCertificateValidAccordingToPteidSDK != userData.isAuthenticationCertificateValidAccordingToPteidSDK)
            return false;
        if (!fullName.equals(userData.fullName)) return false;
        if (!civilNumber.equals(userData.civilNumber)) return false;
        return Arrays.equals(encodedAuthenticationCertificate, userData.encodedAuthenticationCertificate);
    }

    @Override
    public int hashCode() {
        int result = fullName.hashCode();
        result = 31 * result + civilNumber.hashCode();
        result = 31 * result + Arrays.hashCode(encodedAuthenticationCertificate);
        result = 31 * result + (isAuthenticationCertificateValidAccordingToPteidSDK ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "fullName='" + fullName + '\'' +
                ", civilNumber='" + civilNumber + '\'' +
                ", encodedAuthenticationCertificate=" + Arrays.toString(encodedAuthenticationCertificate) +
                ", isAuthenticationCertificateValidAccordingToPteidSDK=" + isAuthenticationCertificateValidAccordingToPteidSDK +
                '}';
    }
}
