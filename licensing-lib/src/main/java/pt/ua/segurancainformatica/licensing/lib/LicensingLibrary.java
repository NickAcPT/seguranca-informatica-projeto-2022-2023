package pt.ua.segurancainformatica.licensing.lib;

/**
 * Interface principal da biblioteca de licenciamento.
 */
public interface LicensingLibrary {

    /**
     * Inicializa a biblioteca de licenciamento.
     * @param appName Nome da aplicação.
     * @param version Versão da aplicação.
     */
    void init(String appName, String version);

    /**
     * Verifica se a aplicação está licenciada.
     * @return {@code true} se a aplicação estiver licenciada, {@code false} caso contrário.
     */
    boolean isRegistered();

    /**
     * Inicia o processo de registo da aplicação.
     * @return {@code true} se a aplicação estiver licenciada, {@code false} caso contrário.
     */
    boolean startRegistration();

    /**
     * Apresenta os detalhes da licença da aplicação.
     */
    void showLicenseInfo();

}
