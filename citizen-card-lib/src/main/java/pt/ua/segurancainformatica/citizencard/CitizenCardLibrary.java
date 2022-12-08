package pt.ua.segurancainformatica.citizencard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.impl.CitizenCardLibraryImpl;
import pt.ua.segurancainformatica.citizencard.mock.CitizenCardLibraryMock;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.security.Provider;
import java.util.ArrayList;

/**
 * Library for interacting with the Portuguese Citizen Card.
 * <p>
 * This serves as as wrapper for the PTeID Java library.
 */
public interface CitizenCardLibrary extends AutoCloseable{

    /**
     * Gets an instance of the Citizen Card Library.
     * @return The singleton instance of the Citizen Card Library.
     */
    @NotNull
    static CitizenCardLibrary citizenCardLibrary() {
        if (System.getProperty("MOCK_CITIZEN_CARD", "false").equals("true")) {
            return new CitizenCardLibraryMock(System.getProperty("HAS_MOCK_CARD", "false").equals("true"));
        }
        return new CitizenCardLibraryImpl();
    }

    @Nullable CitizenCard readCitizenCard();

    void registerCitizenCardListener(@NotNull CitizenCardListener listener);

    void unregisterCitizenCardListener(@NotNull CitizenCardListener listener);

    ArrayList<CitizenCardListener> getListeners();

    Provider getProvider();
}
