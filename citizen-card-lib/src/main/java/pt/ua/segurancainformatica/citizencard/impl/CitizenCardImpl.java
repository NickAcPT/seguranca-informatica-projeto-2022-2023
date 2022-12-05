package pt.ua.segurancainformatica.citizencard.impl;

import org.jetbrains.annotations.Nullable;
import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.ua.segurancainformatica.citizencard.CitizenCardException;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayDeque;

public class CitizenCardImpl implements CitizenCard {

    private final String name;
    private final String number;
    private final Certificate[] authenticationCertificateChain;

    public CitizenCardImpl(PTEID_EIDCard card) throws PTEID_Exception, CertificateException {
        name = card.getID().getGivenName() + ' ' + card.getID().getSurname();
        number = card.getID().getCivilianIdNumber();
        authenticationCertificateChain = loadAuthenticationCertificateChain(card);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCivilNumber() {
        return number;
    }

    @Override
    public KeyPair getAuthenticationKeyPair() {
        return new KeyPair(getAuthenticationPublicKey(), getAuthenticationPrivateKey());
    }

    @Override
    public Certificate getAuthenticationCertificate() throws CitizenCardException {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS11", CitizenCardLibraryImpl.INSTANCE.getProvider());
            keyStore.load(null, null);

            return keyStore.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE");
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Certificate @Nullable [] getAuthenticationCertificateChain() {
        return authenticationCertificateChain;
    }

    @Override
    public PublicKey getAuthenticationPublicKey() {
        return getAuthenticationCertificate().getPublicKey();
    }

    @Override
    public PrivateKey getAuthenticationPrivateKey() throws CitizenCardException {
        try {
            var keyStore = KeyStore.getInstance("PKCS11", CitizenCardLibraryImpl.INSTANCE.getProvider());
            keyStore.load(null, null);

            return ((PrivateKey) keyStore.getKey("CITIZEN AUTHENTICATION CERTIFICATE", null));
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException e) {
            throw new CitizenCardException(e);
        }
    }

    private Certificate[] loadAuthenticationCertificateChain(PTEID_EIDCard card) throws PTEID_Exception, CertificateException {
        var certificateFactory = CertificateFactory.getInstance("X.509");
        var chain = new ArrayDeque<Certificate>();

        var current = card.getAuthentication();
        while (current != null) {
            var bytes = current.getCertData().GetBytes();
            chain.addFirst(certificateFactory.generateCertificate(new ByteArrayInputStream(bytes)));

            if (current.isRoot()) break;
            current = current.getIssuer();
        }

        return chain.toArray(new Certificate[0]);
    }
}
