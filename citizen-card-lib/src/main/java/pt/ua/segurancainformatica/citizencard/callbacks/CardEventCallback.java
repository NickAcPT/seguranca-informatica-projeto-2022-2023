package pt.ua.segurancainformatica.citizencard.callbacks;

import org.jetbrains.annotations.NotNull;
import pt.gov.cartaodecidadao.Callback;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;

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
            System.out.println("Card inserted");
            for (CitizenCardListener citizenCard : citizenCardLibrary.getListeners()) {
                citizenCard.onCardInserted();
            }
        } else {
            // Card removed
            System.out.println("Card removed");
            for (CitizenCardListener citizenCard : citizenCardLibrary.getListeners()) {
                citizenCard.onCardRemoved();
            }
        }
    }

}
