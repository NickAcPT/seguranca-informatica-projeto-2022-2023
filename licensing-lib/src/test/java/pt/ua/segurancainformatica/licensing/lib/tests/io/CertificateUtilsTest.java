package pt.ua.segurancainformatica.licensing.lib.tests.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.ua.segurancainformatica.licensing.common.utils.CertificateUtils;

class CertificateUtilsTest extends CitizenCardRequiringTest {
    @Test
    public void testCertificateCheck() {
        Assertions.assertDoesNotThrow(() -> {
            if (card != null) {
                Assertions.assertTrue(CertificateUtils.isValid(card.getAuthenticationCertificate()));
            }
        });
    }
}