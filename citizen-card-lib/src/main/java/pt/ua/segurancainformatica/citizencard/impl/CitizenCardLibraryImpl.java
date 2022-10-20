package pt.ua.segurancainformatica.citizencard.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.gov.cartaodecidadao.PTEID_ReaderContext;
import pt.gov.cartaodecidadao.PTEID_ReaderSet;
import pt.ua.segurancainformatica.citizencard.CitizenCardException;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.util.ArrayList;

public class CitizenCardLibraryImpl implements CitizenCardLibrary, AutoCloseable {

    public static final CitizenCardLibrary INSTANCE = new CitizenCardLibraryImpl();

    private final ArrayList<CitizenCardListener> listeners = new ArrayList<>();

    private CitizenCardLibraryImpl() {
        try {
            System.loadLibrary("pteidlibj");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. \n" + e);
            System.exit(1);
        }

        try {
            PTEID_ReaderSet.initSDK();
        } catch (PTEID_Exception e) {
            throw new CitizenCardException(e);
        }
    }

    @Override
    public @Nullable CitizenCard readCitizenCard() {
        try {
            PTEID_ReaderContext readerContext = PTEID_ReaderSet.instance().getReader();
            PTEID_EIDCard card = readerContext.getEIDCard();

            if (card != null) {
                return new CitizenCardImpl(card);
            }
        } catch (PTEID_Exception e) {
            throw new CitizenCardException(e);
        }
        return null;
    }

    @Override
    public void registerCitizenCardListener(@NotNull CitizenCardListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterCitizenCardListener(@NotNull CitizenCardListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void close() throws Exception {
        PTEID_ReaderSet.releaseSDK();
    }
}
