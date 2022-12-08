package pt.ua.segurancainformatica.citizencard.mock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class CitizenCardLibraryMock implements CitizenCardLibrary {
    private @Nullable CitizenCard citizenCard;

    public CitizenCardLibraryMock(boolean hasCard) {
        System.out.println("Using mock citizen card library");
        System.out.println("Has card: " + hasCard);
        if (hasCard) {
            try {
                citizenCard = new MockCitizenCard();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public @Nullable CitizenCard readCitizenCard() {
        return citizenCard;
    }

    @Override
    public void registerCitizenCardListener(@NotNull CitizenCardListener listener) {
        if (citizenCard != null) {
            listener.onCardInserted();
        }
    }

    @Override
    public void unregisterCitizenCardListener(@NotNull CitizenCardListener listener) {

    }

    @Override
    public ArrayList<CitizenCardListener> getListeners() {
        return new ArrayList<>();
    }

    @Override
    public Provider getProvider() {
        return Security.getProvider("SunPKCS11");
    }

    @Override
    public void close() {

    }
}
