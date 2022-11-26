package pt.ua.segurancainformatica.licensing.common.tests.wrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;
import pt.ua.segurancainformatica.licensing.common.model.request.LicenseRequest;
import pt.ua.segurancainformatica.licensing.common.wrapper.SecureWrapper;
import pt.ua.segurancainformatica.licensing.common.wrapper.pipeline.SecureWrapperPipelineContext;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

class SecureWrapperTest {

    private static SecureWrapperPipelineContext context;
    private static LicenseRequest request;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
        aesGenerator.init(256);

        var keyPair = generator.generateKeyPair();
        var cipherKey = aesGenerator.generateKey();

        context = SecureWrapper.createContext(LicenseRequest.class, keyPair.getPrivate(), cipherKey);
        request = new LicenseRequest(
                new UserData("Sample User", "123456789", keyPair.getPublic()),
                new ApplicationInformation("Sample App", "1.0.0", new byte[0]));
    }

    @Test
    void wrapObject() {
        Assertions.assertDoesNotThrow(() -> {
            SecureWrapper.wrapObject(request, context);
        });
    }

    @Test
    void unwrapObject() {
        var wrapped = SecureWrapper.wrapObject(request, context);
        var unwrapped = Assertions.assertDoesNotThrow((ThrowingSupplier<LicenseRequest>) () -> SecureWrapper.unwrapObject(wrapped, context));
        Assertions.assertEquals(request, unwrapped);
    }
}