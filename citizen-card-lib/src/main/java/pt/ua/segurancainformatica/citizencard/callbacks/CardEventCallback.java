package pt.ua.segurancainformatica.citizencard.callbacks;

import org.jetbrains.annotations.NotNull;
import pt.gov.cartaodecidadao.Callback;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.util.Objects;

public class CardEventCallback implements Callback {

    private final @NotNull CitizenCardLibrary citizenCardLibrary;

    public CardEventCallback(@NotNull CitizenCardLibrary citizenCardLibrary) {
        this.citizenCardLibrary = citizenCardLibrary;
    }

    @Override
    public void getEvent(long lRet, long ulState, Object callbackData) {
        int cardState = (int) ulState & 0x0000FFFF;
        if ((cardState & 0x0100) != 0) {
            // Card inserted
            CitizenCard card = citizenCardLibrary.readCitizenCard();
            for (CitizenCardListener citizenCard : citizenCardLibrary.getListeners()) {
                citizenCard.onCardInserted(Objects.requireNonNull(card));
            }
        } else {
            // Card removed
            for (CitizenCardListener citizenCard : citizenCardLibrary.getListeners()) {
                citizenCard.onCardRemoved();
            }
        }
    }

}
