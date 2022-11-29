package pt.ua.segurancainformatica.citizencard.impl;

import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.ua.segurancainformatica.citizencard.CitizenCardException;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class CitizenCardImpl implements CitizenCard {

    private final String name;
    private final String number;

    public CitizenCardImpl(PTEID_EIDCard card) throws PTEID_Exception {
        name = card.getID().getGivenName() + card.getID().getSurname();
        number = card.getID().getCivilianIdNumber();
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
    public PublicKey getAuthenticationPublicKey() {
        try {
            var keyStore = KeyStore.getInstance("PKCS11", CitizenCardLibraryImpl.INSTANCE.getProvider());
            keyStore.load(null, null);

            return keyStore.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE").getPublicKey();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new CitizenCardException(e);
        }
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
}
