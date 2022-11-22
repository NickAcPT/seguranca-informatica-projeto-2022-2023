package pt.ua.segurancainformatica.citizencard.model;

import java.security.Key;

/**
 * Object representing a Portuguese Citizen Card.
 * <p>
 * This contains the information that can be read from the card.
 */
public interface CitizenCard {
     String getName();
     String getCivilNumber();

     Key getCitizenAuthenticationCertificate();
}
