package pt.ua.segurancainformatica.citizencard.callbacks;

import pt.gov.cartaodecidadao.Callback;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.util.Objects;

public class CardEventCallback implements Callback {

    @Override
    public void getEvent(long lRet, long ulState, Object callbackData) {
        int cardState = (int) ulState & 0x0000FFFF;
        if ((cardState & 0x0100) != 0) {
            // Card inserted
            CitizenCard card = CitizenCardLibrary.citizenCardLibrary().readCitizenCard();
            for (CitizenCardListener citizenCard : CitizenCardLibrary.citizenCardLibrary().getListeners()) {
                citizenCard.onCardInserted(Objects.requireNonNull(card));
            }
        } else {
            // Card removed
            for (CitizenCardListener citizenCard : CitizenCardLibrary.citizenCardLibrary().getListeners()) {
                citizenCard.onCardRemoved();
            }
        }
    }

}
