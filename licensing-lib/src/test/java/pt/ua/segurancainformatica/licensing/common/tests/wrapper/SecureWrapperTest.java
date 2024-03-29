package pt.ua.segurancainformatica.licensing.common.tests.wrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;
import pt.ua.segurancainformatica.licensing.common.model.request.LicenseRequest;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineSide;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

class SecureWrapperTest {

    private static final Random random = new Random();
    private static LicenseRequest request;
    private static KeyPairGenerator keyPairGenerator;
    private static SecureWrapperPipelineContext<LicenseRequest> userContext;
    private static SecureWrapperPipelineContext<LicenseRequest> managerContext;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
        aesGenerator.init(256);

        KeyPair userKeyPair = keyPairGenerator.generateKeyPair();
        KeyPair managerKeyPair = keyPairGenerator.generateKeyPair();

        SecretKey cipherKey = aesGenerator.generateKey();

        userContext = SecureWrapper.createContext(LicenseRequest.class, new KeyPair(
                managerKeyPair.getPublic(),
                null
        ), userKeyPair, cipherKey, SecureWrapperPipelineSide.USER);

        managerContext = SecureWrapper.createContext(LicenseRequest.class, managerKeyPair, null, null, SecureWrapperPipelineSide.MANAGER);
        request = new LicenseRequest(
                new UserData("Sample User", "123456789", userKeyPair.getPublic().getEncoded(), true),
                new ApplicationInformation("Sample App", "1.0.0", "")
        );
    }

    @Test
    void wrapObject() {
        Assertions.assertDoesNotThrow(() -> {
            SecureWrapper.wrapObject(request, userContext);
        });
    }

    @Test
    void unwrapObjectFromManager() {
        Assertions.assertDoesNotThrow(() -> {
            var managerCtx = SecureWrapper.createContext(LicenseRequest.class, managerContext.managerKeyPair(), null, null, SecureWrapperPipelineSide.MANAGER);
            var wrappedFromUser = SecureWrapper.wrapObject(request, userContext);
            var unwrappedFromManager = SecureWrapper.unwrapObject(wrappedFromUser, managerCtx);

            var wrappedFromManager = SecureWrapper.wrapObject(unwrappedFromManager, managerCtx);
            SecureWrapper.unwrapObject(wrappedFromManager, userContext);
        });
    }

    @Test
    void unwrapObjectWithoutFullContext() throws SecureWrapperInvalidatedException {
        var wrapped = SecureWrapper.wrapObject(request, userContext);

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, userContext)
        );
    }

    @Test
    void unwrapObject() throws SecureWrapperInvalidatedException {
        var wrapped = SecureWrapper.wrapObject(request, userContext);
        var unwrapped = Assertions.assertDoesNotThrow(
                () -> SecureWrapper.unwrapObject(wrapped, managerContext));
        Assertions.assertEquals(request, unwrapped);
    }

    @Test
    void unwrapObjectWithIncorrectUserPrivateKey() throws SecureWrapperInvalidatedException {
        var keyPair = keyPairGenerator.generateKeyPair();

        // Wrap the object and sign it with the wrong private key.
        var modifiedContext = new SecureWrapperPipelineContext<>(
                userContext.type(),
                SecureWrapperPipelineSide.USER, userContext.managerKeyPair(),
                new KeyPair(Objects.requireNonNull(userContext.userKeyPair()).getPublic(), /* Bad actor private key */ keyPair.getPrivate()),
                userContext.cipherKey()
        );

        var wrapped = SecureWrapper.wrapObject(request, modifiedContext);

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, managerContext)
        );
    }

    @Test
    void unwrapObjectWithIncorrectUserPublicKey() throws SecureWrapperInvalidatedException {
        var keyPair = keyPairGenerator.generateKeyPair();

        // Wrap the object and sign it with the wrong public key.
        var modifiedContext = new SecureWrapperPipelineContext<>(
                userContext.type(),
                SecureWrapperPipelineSide.USER, userContext.managerKeyPair(),
                new KeyPair(/* Bad actor public key */ keyPair.getPublic(),
                        Objects.requireNonNull(userContext.userKeyPair()).getPrivate()),
                userContext.cipherKey()
        );

        var wrapped = SecureWrapper.wrapObject(request, modifiedContext);

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, managerContext)
        );
    }

    @Test
    void unwrapObjectWithIncorrectManagerPublicKey() throws SecureWrapperInvalidatedException {
        KeyPair managerKeyPair = Objects.requireNonNull(userContext.managerKeyPair());
        var keyPair = keyPairGenerator.generateKeyPair();

        // Wrap the object and sign it with the wrong public key.
        var modifiedContext = new SecureWrapperPipelineContext<>(
                userContext.type(),
                SecureWrapperPipelineSide.USER, new KeyPair(/* Bad actor public key */ keyPair.getPublic(),
                managerKeyPair.getPrivate()),
                userContext.userKeyPair(),
                userContext.cipherKey()
        );

        var wrapped = SecureWrapper.wrapObject(request, modifiedContext);
        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, managerContext)
        );
    }

    @RepeatedTest(1500)
    @Execution(ExecutionMode.CONCURRENT)
    void unwrapObjectThatWasModified() throws SecureWrapperInvalidatedException {
        var wrapped = SecureWrapper.wrapObject(request, userContext);

        // Modify the wrapped object
        // Compute how many bytes to modify (from 1% to 35% of the object)
        int times = random.nextInt((int) (wrapped.length * 0.01), (int) (wrapped.length * 0.35));

        for (int j = 0; j < times; j++) {
            wrapped[random.nextInt(wrapped.length)] = (byte) random.nextInt();
        }

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, managerContext)
        );
    }
}