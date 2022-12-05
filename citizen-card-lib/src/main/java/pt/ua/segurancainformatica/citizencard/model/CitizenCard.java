package pt.ua.segurancainformatica.citizencard.model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * Object representing a Portuguese Citizen Card.
 * <p>
 * This contains the information that can be read from the card.
 */
public interface CitizenCard {
     String getName();

     String getCivilNumber();

     KeyPair getAuthenticationKeyPair();

     PublicKey getAuthenticationPublicKey();

     PrivateKey getAuthenticationPrivateKey();

     Certificate getAuthenticationCertificate();

     boolean isAuthenticationCertificateValid();
}
