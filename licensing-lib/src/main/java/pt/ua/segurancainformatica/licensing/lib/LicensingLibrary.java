package pt.ua.segurancainformatica.licensing.lib;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.lib.impl.LicensingLibraryImpl;

/**
 * Interface principal da biblioteca de licenciamento.
 */
public interface LicensingLibrary extends AutoCloseable {

    static @NotNull LicensingLibrary getInstance() {
        return LicensingLibraryImpl.INSTANCE;
    }

    /**
     * Inicializa a biblioteca de licenciamento.
     * @param appName Nome da aplicação.
     * @param version Versão da aplicação.
     */
    void init(@NotNull String appName, @NotNull String version) throws LicensingException;

    /**
     * Verifica se a aplicação está licenciada.
     * @return {@code true} se a aplicação estiver licenciada, {@code false} caso contrário.
     */
    boolean isRegistered();

    /**
     * Inicia o processo de registo da aplicação.
     * @return {@code true} se a aplicação estiver licenciada, {@code false} caso contrário.
     */
    boolean startRegistration() throws LicensingException;

    /**
     * Apresenta os detalhes da licença da aplicação.
     */
    void showLicenseInfo();

}
