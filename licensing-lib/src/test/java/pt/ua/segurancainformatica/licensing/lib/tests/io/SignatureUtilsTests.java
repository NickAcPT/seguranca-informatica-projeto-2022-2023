package pt.ua.segurancainformatica.licensing.lib.tests.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ua.segurancainformatica.licensing.lib.io.SignatureUtils;

public class SignatureUtilsTests extends CitizenCardRequiringTest {

    @Test
    public void testSignBlob() {
        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};

        Assertions.assertDoesNotThrow(() -> {
            if (card != null) {
                SignatureUtils.signBlob(card, bytes);
            }
        });
    }
}
