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
import pt.ua.segurancainformatica.licensing.common.utils.SignatureUtils;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineSide;
import pt.ua.segurancainformatica.licensing.lib.LicensingAlertor;
import pt.ua.segurancainformatica.licensing.lib.LicensingConstants;
import pt.ua.segurancainformatica.licensing.lib.LicensingException;
import pt.ua.segurancainformatica.licensing.lib.LicensingLibrary;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class LicensingLibraryImpl implements LicensingLibrary, CitizenCardListener {
    public static final LicensingLibrary INSTANCE = new LicensingLibraryImpl();
    private final @NotNull CitizenCardLibrary citizenCardLibrary = CitizenCardLibrary.citizenCardLibrary();
    private final SecureRandom secureRandom = new SecureRandom();
    private @Nullable PublicKey managerPublicKey;
    private @Nullable ApplicationInformation currentApplicationInformation;
    private @Nullable LicenseInformation currentLicenseInformation;
    private @Nullable CitizenCard currentCitizenCard = null;
    private @Nullable Thread userCheckThread;
    private @Nullable LicensingAlertor alertor;

    private LicensingLibraryImpl() {
        citizenCardLibrary.registerCitizenCardListener(this);
    }

    @Override
    public void onCardInserted() {
        currentCitizenCard = citizenCardLibrary.readCitizenCard();
    }

    @Override
    public void onCardRemoved() {
        boolean needsToClose = currentCitizenCard != null;
        currentCitizenCard = null;
        if (needsToClose) {
            Objects.requireNonNull(alertor).showLicensingAlert("Cartão de Cidadão removido!\nA aplicação irá encerrar.", () -> {
                try {
                    close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            });

        }
    }

    @Override
    public boolean init(@NotNull String appName, @NotNull String version, @NotNull LicensingAlertor alertor) throws LicensingException {
        readManagerPublicKey();
        this.alertor = alertor;
        currentCitizenCard = citizenCardLibrary.readCitizenCard();
        try {
            currentApplicationInformation = new ApplicationInformation(appName, version, HashingCommon.getCurrentJarHash());
            readLicenseInformation();
            var isLicensed = checkLicensing(alertor);
            if (isLicensed) {
                startUserCheckThread();
            }
            return isLicensed;
        } catch (HashingException e) {
            throw new LicensingException("Failed to get current jar hash", e);
        } catch (GeneralSecurityException e) {
            throw new LicensingException("Failed to read license information", e);
        }
    }

    private void startUserCheckThread() throws GeneralSecurityException {
        verifyUser();

        userCheckThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(LicensingConstants.LICENSE_REQUEST_TIMEOUT.toMillis());

                    verifyUser();
                } catch (InterruptedException | GeneralSecurityException e) {
                    if (!(e instanceof InterruptedException)) throw new RuntimeException(e);
                }
            }
        });
        userCheckThread.start();
    }

    public void verifyUser() throws GeneralSecurityException {
        if (currentCitizenCard != null) {
            System.out.println("Checking user...");
            var bytes = new byte[32];
            secureRandom.nextBytes(bytes);

            SignatureUtils.signBlob(currentCitizenCard.getAuthenticationPrivateKey(), bytes);
            System.out.println("User is valid");
        }
    }

    private boolean checkLicensing(@NotNull LicensingAlertor alertor) throws LicensingException {
        if (!isRegistered()) {
            if (Files.exists(LicensingConstants.LICENSE_REQUEST_PATH)) {
                alertor.showLicensingAlert("""
                        Você já criou um pedido de licença para esta aplicação!
                        Por favor envie o pedido ao administrador para obter a sua licença.""");
                return false;
            } else if (alertor.showYesNoAlert("Licenciamento", """
                    A licença da aplicação não é válida ou não existe!
                    Deseja iniciar o processo de registo?
                                        
                    O processo de registo requer a presença de um cartão de cidadão e requer o seu PIN de autenticação.

                    Caso não seja licenciada, a aplicação irá ser encerrada!""")) {
                startRegistration();
                return false;
            } else {
                return false;
            }
        }
        return true;
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
        if (currentLicenseInformation == null || currentCitizenCard == null || currentApplicationInformation == null) {
            return false;
        }

        var user = currentLicenseInformation.user();
        var userDataFromCitizenCard = LicensingCommon.getUserDataFromCitizenCard(currentCitizenCard);
        if (!user.equals(userDataFromCitizenCard)) {
            System.out.println("Citizen card data does not match license data");
            return false;
        }

        var application = currentLicenseInformation.application();
        if (!application.equals(currentApplicationInformation)) {
            System.out.println("Application data does not match license data");
            return false;
        }

        if (checkIsMismatch(
                currentLicenseInformation.computer().environmentVariables(),
                LicensingConstants.MISMATCH_ENVIRONMENT_VARIABLE_PERCENTAGE,
                e -> !e.matches())
        ) {
            System.out.println("Environment variables do not match license data");
            return false;
        }


        if (checkIsMismatch(
                currentLicenseInformation.computer().networkInterfaces(),
                LicensingConstants.MISMATCH_NETWORK_INTERFACE_PERCENTAGE,
                e -> !e.matches())
        ) {
            System.out.println("Network interfaces do not match license data");
            return false;
        }

        var now = Instant.now();

        if (currentLicenseInformation.license().endDate().isBefore(now)) {
            System.out.println("License has expired");
            return false;
        }

        if (currentLicenseInformation.license().startDate().isAfter(now)) {
            System.out.println("License is not valid yet");
            return false;
        }

        return true;
    }

    private <T> boolean checkIsMismatch(@NotNull T @NotNull [] value, double percentage, Predicate<@NotNull T> predicate) {
        var mismatchEnvVars = Arrays.stream(value).filter(predicate).count();
        var envVars = value.length;
        return (double) mismatchEnvVars / envVars > percentage;
    }

    @Override
    public void startRegistration() throws LicensingException {
        if (currentCitizenCard == null) {
            throw new LicensingException("Não foi possível iniciar o processo de registo!\n" +
                    "Não foi possivel detetar o cartão de cidadão.");
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

            throw new LicensingException("O processo de registo foi finalizado com sucesso!\n" +
                    "Por favor, contacte o administrador do sistema para continuar o processo.");
        } catch (SecureWrapperInvalidatedException e) {
            throw new LicensingException("Não foi possível efectuar o processo de registo!\n" +
                    "O sistema de segurança foi invalidado.", e);
        } catch (IOException e) {
            throw new LicensingException("Não foi possível efectuar o processo de registo!\n" +
                    "Não foi possível escrever o ficheiro do pedido.", e);
        }
    }

    @Override
    public void showLicenseInfo() {
        var app = Objects.requireNonNull(currentLicenseInformation).application();
        var user = Objects.requireNonNull(currentLicenseInformation).user();
        var license = Objects.requireNonNull(currentLicenseInformation).license();

        Objects.requireNonNull(alertor).showLicensingAlert("""
                Licença de utilização da aplicação %s (versão %s; hash %s).

                Licenciado para %s (%s).
                Licença válida de %s até %s (%s dia(s) restante(s)).""".formatted(
                app.name(),
                app.version(),
                app.hash(),

                user.fullName(),
                user.civilNumber(),

                license.startDate(),
                license.endDate(),

                Instant.now().until(license.endDate(), ChronoUnit.DAYS)
        ));
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
                    "Não foi detetado nenhum Cartão de Cidadão.");
        }

        try {
            var context = createSecureWrapperContext(LicenseInformation.class, currentCitizenCard);
            var licenseFileBytes = Files.readAllBytes(LicensingConstants.LICENSE_FILE_PATH);
            currentLicenseInformation = SecureWrapper.unwrapObject(licenseFileBytes, context);
        } catch (SecureWrapperInvalidatedException e) {
            throw new LicensingException("""
                    A sua licença não é legítima!
                    Por favor contacte o administrador.
                    O sistema de segurança foi invalidado.""", e);
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

        try {
            KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
            aesGenerator.init(256);
            SecretKey cipherKey = aesGenerator.generateKey();

            return new SecureWrapperPipelineContext<>(
                    clazz,
                    SecureWrapperPipelineSide.USER, new KeyPair(managerPublicKey, null),
                    citizenCard.getAuthenticationKeyPair(),
                    cipherKey
            );
        } catch (NoSuchAlgorithmException e) {
            throw new LicensingException("Erro interno: Não foi possível criar uma chave para a cifra AES.", e);
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("Closing licensing manager");
        if (userCheckThread != null) {
            userCheckThread.interrupt();
        }
        citizenCardLibrary.close();
    }

}
