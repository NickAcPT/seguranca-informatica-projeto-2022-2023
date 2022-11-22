package pt.ua.segurancainformatica.licensing.lib.tests.io;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import pt.ua.segurancainformatica.citizencard.impl.CitizenCardLibraryImpl;
import pt.ua.segurancainformatica.citizencard.model.CitizenCard;

public class CitizenCardRequiringTest {
    static @Nullable CitizenCard card;

    @BeforeAll
    static void setUp() {
        card = CitizenCardLibraryImpl.INSTANCE.readCitizenCard();
    }
}
