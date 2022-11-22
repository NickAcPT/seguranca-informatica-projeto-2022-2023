package pt.ua.segurancainformatica.citizencard.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.gov.cartaodecidadao.PTEID_EIDCard;
import pt.gov.cartaodecidadao.PTEID_Exception;
import pt.gov.cartaodecidadao.PTEID_ReaderContext;
import pt.gov.cartaodecidadao.PTEID_ReaderSet;
import pt.ua.segurancainformatica.citizencard.CitizenCardException;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.callbacks.CardEventCallback;
import pt.ua.segurancainformatica.citizencard.callbacks.CitizenCardListener;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class CitizenCardLibraryImpl implements CitizenCardLibrary, AutoCloseable {

    public static final CitizenCardLibrary INSTANCE = new CitizenCardLibraryImpl();

    private final ArrayList<CitizenCardListener> listeners = new ArrayList<>();
    private final long eventCallbackId;
    private final PTEID_ReaderContext context;

    private CitizenCardLibraryImpl() {
        try {
            System.loadLibrary("pteidlibj");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. \n" + e);
            System.exit(1);
        }

        try {
            PTEID_ReaderSet.initSDK();
            PTEID_ReaderSet readerSet = PTEID_ReaderSet.instance();
            context = readerSet.getReader();
            eventCallbackId = context.SetEventCallback(new CardEventCallback(), null);
        } catch (PTEID_Exception e) {
            throw new CitizenCardException(e);
        }
    }

    @Override
    public @Nullable CitizenCard readCitizenCard() {
        try {
            PTEID_EIDCard card = context.getEIDCard();

            if (card != null) {
                return new CitizenCardImpl(card);
            }
        } catch (PTEID_Exception e) {
            return null;
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
    public ArrayList<CitizenCardListener> getListeners() {
        return listeners;
    }

    @Override
    public void close() throws Exception {
        context.StopEventCallback(eventCallbackId);
        PTEID_ReaderSet.releaseSDK();
    }

    @Override
    public Provider getProvider() {
        return Security.getProvider("SunPKCS11-CartaoCidadao");
    }
}
