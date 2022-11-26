package pt.ua.segurancainformatica.licensing.common.tests.wrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;
import pt.ua.segurancainformatica.licensing.common.model.request.LicenseRequest;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapperInvalidatedException;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

class SecureWrapperTest {

    private static final Random random = new Random();
    private static SecureWrapperPipelineContext context;
    private static LicenseRequest request;
    private static KeyPair userKeyPair;
    private static KeyPair gestorKeyPair;
    private static SecretKey cipherKey;
    private static KeyPairGenerator keyPairGenerator;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
        aesGenerator.init(256);

        userKeyPair = keyPairGenerator.generateKeyPair();
        gestorKeyPair = keyPairGenerator.generateKeyPair();

        cipherKey = aesGenerator.generateKey();

        context = SecureWrapper.createContext(LicenseRequest.class, gestorKeyPair.getPublic(), userKeyPair, cipherKey);
        request = new LicenseRequest(
                new UserData("Sample User", "123456789", userKeyPair.getPublic()),
                new ApplicationInformation("Sample App", "1.0.0", new byte[0])
        );
    }

    @Test
    void wrapObject() {
        Assertions.assertDoesNotThrow(() -> {
            SecureWrapper.wrapObject(request, context);
        });
    }

    @Test
    void unwrapObject() throws SecureWrapperInvalidatedException {
        var wrapped = SecureWrapper.wrapObject(request, context);
        var unwrapped = Assertions.assertDoesNotThrow(
                (ThrowingSupplier<LicenseRequest>) () -> SecureWrapper.unwrapObject(wrapped, context));
        Assertions.assertEquals(request, unwrapped);
    }

    @Test
    void unwrapObjectWithIncorrectPrivateKey() throws SecureWrapperInvalidatedException {
        var keyPair = keyPairGenerator.generateKeyPair();

        // Wrap the object and sign it with the wrong private key
        var modifiedContext = new SecureWrapperPipelineContext(
                context.type(),
                context.managerPublicKey(),
                new KeyPair(context.userKeyPair().getPublic(), keyPair.getPrivate()),
                context.cipherKey()
        );

        var wrapped = SecureWrapper.wrapObject(request, modifiedContext);

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, context)
        );
    }

    @Test
    void unwrapObjectWithIncorrectPublicKey() throws SecureWrapperInvalidatedException {
        var keyPair = keyPairGenerator.generateKeyPair();

        // Wrap the object and sign it with the wrong key pair
        var modifiedContext = new SecureWrapperPipelineContext(
                context.type(),
                context.managerPublicKey(),
                new KeyPair(
                        keyPair.getPublic(),
                        context.userKeyPair().getPrivate()
                ),
                context.cipherKey()
        );

        var wrapped = SecureWrapper.wrapObject(request, modifiedContext);

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, context)
        );
    }

    @RepeatedTest(10000)
    @Execution(ExecutionMode.CONCURRENT)
    void unwrapObjectThatWasModified() throws SecureWrapperInvalidatedException {
        var wrapped = SecureWrapper.wrapObject(request, context);

        // Modify the wrapped object
        // Compute how many bytes to modify (from 1% to 35% of the object)
        int times = random.nextInt((int) (wrapped.length * 0.01), (int) (wrapped.length * 0.35));

        for (int j = 0; j < times; j++) {
            wrapped[random.nextInt(wrapped.length)] = (byte) random.nextInt();
        }

        Assertions.assertThrows(
                SecureWrapperInvalidatedException.class,
                () -> SecureWrapper.unwrapObject(wrapped, context)
        );
    }
}