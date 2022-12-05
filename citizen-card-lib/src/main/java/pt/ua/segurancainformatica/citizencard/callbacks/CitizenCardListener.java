package pt.ua.segurancainformatica.citizencard.callbacks;

/**
 * Listener for the Citizen Card.
 */
public interface CitizenCardListener{

    /**
     * Called when the Citizen Card gets inserted in the card reader.
     */
    default void onCardInserted() {
    }

    /**
     * Called when the Citizen Card gets removed from the card reader.
     */
    default void onCardRemoved() {
    }
}
