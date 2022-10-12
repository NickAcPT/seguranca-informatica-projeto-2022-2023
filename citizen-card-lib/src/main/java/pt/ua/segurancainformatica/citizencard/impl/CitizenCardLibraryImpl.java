package pt.ua.segurancainformatica.citizencard.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

public class CitizenCardLibraryImpl implements CitizenCardLibrary {

    public static final CitizenCardLibrary INSTANCE = new CitizenCardLibraryImpl();

    private CitizenCardLibraryImpl() {
    }

    @Override
    public @Nullable CitizenCard readCitizenCard() {
        return null;
    }

    @Override
    public void registerCitizenCardListener(@NotNull CitizenCardListener listener) {

    }

    @Override
    public void unregisterCitizenCardListener(@NotNull CitizenCardListener listener) {

    }
}
