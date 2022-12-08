package pt.ua.segurancainformatica.citizencard.mock;

import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class MockCitizenCard implements CitizenCard {
    private final KeyPair authKeyPair;
    public MockCitizenCard() throws NoSuchAlgorithmException {

        var pubKey = Path.of("mock-cc.pub");
        var privKey = Path.of("mock-cc.priv");
        if (Files.exists(pubKey)) {
            var factory = KeyFactory.getInstance("RSA");

            try {
                authKeyPair = new KeyPair(factory.generatePublic(new X509EncodedKeySpec(Files.readAllBytes(pubKey))),
                        factory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privKey))));
            } catch (InvalidKeySpecException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            authKeyPair = keyPairGenerator.generateKeyPair();

            try {
                Files.write(pubKey, authKeyPair.getPublic().getEncoded());
                Files.write(privKey, authKeyPair.getPrivate().getEncoded());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getName() {
        return System.getProperty("MOCK_CITIZEN_CARD_NAME", "Mock Citizen Card");
    }

    @Override
    public String getCivilNumber() {
        return System.getProperty("MOCK_CITIZEN_CARD_CIVIL_NUMBER", "123456789");
    }

    @Override
    public KeyPair getAuthenticationKeyPair() {
        return authKeyPair;
    }

    @Override
    public PublicKey getAuthenticationPublicKey() {
        return authKeyPair.getPublic();
    }

    @Override
    public PrivateKey getAuthenticationPrivateKey() {
        return authKeyPair.getPrivate();
    }

    @Override
    public Certificate getAuthenticationCertificate() {
        return new Certificate("X.509") {
            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }

            @Override
            public void verify(PublicKey key) {
            }

            @Override
            public void verify(PublicKey key, String sigProvider) {

            }

            @Override
            public String toString() {
                return "Mock Certificate";
            }

            @Override
            public PublicKey getPublicKey() {
                return authKeyPair.getPublic();
            }
        };
    }

    @Override
    public boolean isAuthenticationCertificateValid() {
        return true;
    }
}
