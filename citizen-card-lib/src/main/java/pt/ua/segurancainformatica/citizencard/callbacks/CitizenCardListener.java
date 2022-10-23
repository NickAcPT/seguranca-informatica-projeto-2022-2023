package pt.ua.segurancainformatica.citizencard.callbacks;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

/**
 * Listener for the Citizen Card.
 */
public interface CitizenCardListener{

    /**
     * Called when the Citizen Card gets inserted in the card reader.
     */
    default void onCardInserted(@NotNull CitizenCard card) {
    }

    /**
     * Called when the Citizen Card gets removed from the card reader.
     */
    default void onCardRemoved() {
    }
}
