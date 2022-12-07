package pt.ua.segurancainformatica.licensing.lib;

/**
 * Something that can show a licensing alert.
 */
public interface LicensingAlertor {

    /**
     * Shows a licensing alert.
     * @param message The message to show.
     */
    void showLicensingAlert(String message);

    boolean showYesNoAlert(String title, String message);
}
