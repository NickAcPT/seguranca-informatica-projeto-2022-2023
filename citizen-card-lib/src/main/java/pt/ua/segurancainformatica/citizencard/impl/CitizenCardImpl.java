package pt.ua.segurancainformatica.citizencard.impl;

import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

public class CitizenCardImpl implements CitizenCard {


    private final String name;

    public CitizenCardImpl(PTEID_EIDCard card) throws PTEID_Exception {
        name = card.getID().getGivenName() + card.getID().getSurname();
    }

    @Override
    public String getName() {
        return name;
    }
}
