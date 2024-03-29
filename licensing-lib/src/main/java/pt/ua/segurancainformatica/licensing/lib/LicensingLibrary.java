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
     *
     * @param appName Nome da aplicação.
     * @param version Versão da aplicação.
     * @return {@code true} se a inicialização foi bem sucedida e a aplicação pode continuar a execução, {@code false} caso contrário.
     */
    boolean init(@NotNull String appName, @NotNull String version, @NotNull LicensingAlertor alertor) throws LicensingException;

    /**
     * Verifica se a aplicação está licenciada.
     * @return {@code true} se a aplicação estiver licenciada, {@code false} caso contrário.
     */
    boolean isRegistered();

    /**
     * Inicia o processo de registo da aplicação.
     */
    void startRegistration() throws LicensingException;

    /**
     * Apresenta os detalhes da licença da aplicação.
     */
    void showLicenseInfo();

}
