package pt.ua.segurancainformatica.licensing.lib.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;
import pt.ua.segurancainformatica.licensing.common.LicensingCommon;
import pt.ua.segurancainformatica.licensing.common.hashing.HashingCommon;
import pt.ua.segurancainformatica.licensing.common.hashing.HashingException;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.ComputerInformation;
import pt.ua.segurancainformatica.licensing.common.model.license.LicenseInformation;
import pt.ua.segurancainformatica.licensing.common.model.request.LicenseRequest;
import pt.ua.segurancainformatica.licensing.common.utils.KeyUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.lib.LicensingAlertor;
import pt.ua.segurancainformatica.licensing.lib.LicensingConstants;
import pt.ua.segurancainformatica.licensing.lib.LicensingException;
import pt.ua.segurancainformatica.licensing.lib.LicensingLibrary;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;

public class LicensingLibraryImpl implements LicensingLibrary, CitizenCardListener {
    public static final LicensingLibrary INSTANCE = new LicensingLibraryImpl();
    private final @NotNull CitizenCardLibrary citizenCardLibrary = CitizenCardLibrary.citizenCardLibrary();
    private @Nullable PublicKey managerPublicKey;
    private @Nullable ApplicationInformation currentApplicationInformation;
    private @Nullable LicenseInformation currentLicenseInformation;
    private @Nullable CitizenCard currentCitizenCard = null;

    private LicensingLibraryImpl() {
        citizenCardLibrary.registerCitizenCardListener(this);
    }

    @Override
    public void onCardInserted() {
        currentCitizenCard = citizenCardLibrary.readCitizenCard();
    }

    @Override
    public void onCardRemoved() {
        currentCitizenCard = null;
    }

    @Override
    public void init(@NotNull String appName, @NotNull String version, @NotNull LicensingAlertor alertor) throws LicensingException {
        readManagerPublicKey();
        try {
            currentApplicationInformation = new ApplicationInformation(appName, version, HashingCommon.getCurrentJarHash());
            readLicenseInformation();
            checkLicensing(alertor);
        } catch (HashingException e) {
            throw new LicensingException("Failed to get current jar hash", e);
        }
    }

    private void checkLicensing(@NotNull LicensingAlertor alertor) throws LicensingException {
        if (!isRegistered()) {
            if (alertor.showYesNoAlert("Licenciamento", "A aplicação não está licenciada.\n" +
                    "Deseja iniciar o processo de registo?")) {
                startRegistration();
            }
        }
    }

    private void readManagerPublicKey() throws LicensingException {
        try (var managerKeyStream = LicensingLibrary.class.getResourceAsStream("/manager-public.key")) {
            if (managerKeyStream == null) {
                throw new LicensingException("Chave pública do gestor não encontrada.\n" +
                        "Por favor, contacte o administrador do sistema.");
            }

            managerPublicKey = KeyUtils.getPublicKeyFromBytes(managerKeyStream.readAllBytes());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            throw new LicensingException("Failed to read manager public key", e);
        }
    }

    @Override
    public boolean isRegistered() {
        return currentLicenseInformation != null && currentLicenseInformation.isValid();
    }

    @Override
    public void startRegistration() throws LicensingException {
        if (currentCitizenCard == null) {
            throw new LicensingException("Não foi possível iniciar o processo de registo.\n" +
                    "Não foi detetado nenhum Cartão de Cidadão no leitor.");
        }
        if (currentApplicationInformation == null) {
            throw new LicensingException("Não foi possível iniciar o processo de registo.\n" +
                    "Não foi possível obter informação sobre a aplicação.");
        }

        try {
            var userData = LicensingCommon.getUserDataFromCitizenCard(currentCitizenCard);
            var request = new LicenseRequest(userData, currentApplicationInformation, new ComputerInformation());
            var context = createSecureWrapperContext(LicenseRequest.class, currentCitizenCard);

            Files.write(LicensingConstants.LICENSE_REQUEST_PATH, SecureWrapper.wrapObject(request, context));

        } catch (CertificateEncodingException e) {
            throw new LicensingException("Não foi possível iniciar o processo de registo.\n" +
                    "Não foi possível codificar o certificado de autenticação do Cartão de Cidadão.", e);
        } catch (SecureWrapperInvalidatedException e) {
            throw new LicensingException("Não foi possível efectuar o processo de registo.\n" +
                    "O sistema de segurança foi invalidado.", e);
        } catch (IOException e) {
            throw new LicensingException("Não foi possível efectuar o processo de registo.\n" +
                    "Não foi possível escrever o ficheiro do pedido.", e);
        }
    }

    @Override
    public void showLicenseInfo() {
    }

    private void readLicenseInformation() throws LicensingException {
        if (currentApplicationInformation == null) {
            throw new LicensingException("Application information is not initialized");
        }

        if (!Files.exists(LicensingConstants.LICENSE_FILE_PATH)) {
            return;
        }

        if (currentCitizenCard == null) {
            throw new LicensingException("Não foi possível ler a informação sobre a licença.\n" +
                    "Não foi detetado nenhum Cartão de Cidadão no leitor.");
        }

        try {
            var context = createSecureWrapperContext(LicenseInformation.class, currentCitizenCard);
            var licenseFileBytes = Files.readAllBytes(LicensingConstants.LICENSE_FILE_PATH);
            currentLicenseInformation = SecureWrapper.unwrapObject(licenseFileBytes, context);
        } catch (SecureWrapperInvalidatedException e) {
            throw new LicensingException("Não foi possível ler os dados da licença.\n" +
                    "O sistema de segurança foi invalidado.", e);
        } catch (IOException e) {
            throw new LicensingException("Não foi possível ler o ficheiro da licença.", e);
        }
    }

    private <T> SecureWrapperPipelineContext<T> createSecureWrapperContext(Class<T> clazz, @Nullable CitizenCard citizenCard) throws LicensingException {
        if (citizenCard == null) {
            throw new LicensingException(
                    "Não foi possível ler os dados da sua Cartão de Cidadão.\n" +
                            "Por favor, verifique se o seu Cartão de Cidadão está inserido no leitor e se o leitor está ligado ao computador.");
        }

        if (managerPublicKey == null) {
            throw new LicensingException("Manager public key is not initialized");
        }

        return new SecureWrapperPipelineContext<>(
                clazz,
                new KeyPair(managerPublicKey, null),
                citizenCard.getAuthenticationKeyPair(),
                null
        );
    }

    @Override
    public void close() throws Exception {
        citizenCardLibrary.close();
    }
}
