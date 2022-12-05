package pt.ua.segurancainformatica.licensing.lib.tests.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import pt.ua.segurancainformatica.citizencard.CitizenCardLibrary;
import pt.ua.segurancainformatica.citizencard.impl.CitizenCardLibraryImpl;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

public class CitizenCardRequiringTest {
    static @Nullable CitizenCard card;
    private static @NotNull CitizenCardLibrary citizenCardLibrary;

    @BeforeAll
    static void setUp() {
        citizenCardLibrary = CitizenCardLibrary.citizenCardLibrary();
        card = citizenCardLibrary.readCitizenCard();
    }

    @AfterAll
    static void tearDown() throws Exception {
        citizenCardLibrary.close();
    }
}
