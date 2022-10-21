package pt.ua.segurancainformatica.citizencard.impl;

import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

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

}
