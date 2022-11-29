package pt.ua.segurancainformatica.licensing.lib.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;
import pt.ua.segurancainformatica.licensing.common.hashing.HashingCommon;
import pt.ua.segurancainformatica.licensing.common.hashing.HashingException;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.license.LicenseInformation;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.lib.LicensingException;
import pt.ua.segurancainformatica.licensing.lib.LicensingLibrary;

import java.security.KeyPair;

public class LicensingLibraryImpl implements LicensingLibrary, CitizenCardListener {
    public static final LicensingLibrary INSTANCE = new LicensingLibraryImpl();
    private final @NotNull CitizenCardLibrary citizenCardLibrary = CitizenCardLibrary.citizenCardLibrary();
    private @Nullable ApplicationInformation currentApplicationInformation;
    private @Nullable LicenseInformation currentLicenseInformation;

    private LicensingLibraryImpl() {
        citizenCardLibrary.registerCitizenCardListener(this);
    }

    @Override
    public void init(@NotNull String appName, @NotNull String version) throws LicensingException {
        try {
            currentApplicationInformation = new ApplicationInformation(
                    appName, version, HashingCommon.getCurrentJarHash());
            readLicenseInformation();
        } catch (HashingException e) {
            throw new LicensingException("Failed to get current jar hash", e);
        }
    }

    @Override
    public boolean isRegistered() {
        return currentLicenseInformation != null && currentLicenseInformation.isValid();
    }

    @Override
    public boolean startRegistration() {
        return false;
    }

    @Override
    public void showLicenseInfo() {
    }

    private void readLicenseInformation() throws LicensingException {
        if (currentApplicationInformation == null) {
            throw new LicensingException("Application information is not initialized");
        }
    }

    private <T> SecureWrapperPipelineContext<T> createSecureWrapperContext(Class<T> clazz) throws LicensingException {
        @Nullable CitizenCard citizenCard = citizenCardLibrary.readCitizenCard();
        if (citizenCard == null) {
            throw new LicensingException(
                    "Unable to initialize context, citizen card is not available and inserted in the reader.");
        }

        return new SecureWrapperPipelineContext<>(
                clazz,
                new KeyPair(null, null),
                citizenCard.getAuthenticationKeyPair(),
                null
        );
    }

    @Override
    public void close() throws Exception {
        citizenCardLibrary.close();
    }
}
