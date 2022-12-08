package pt.ua.segurancainformatica.licensing.lib;

import org.jetbrains.annotations.Nullable;

/**
 * Something that can show a licensing alert.
 */
public interface LicensingAlertor {

    /**
     * Shows a licensing alert.
     * @param message The message to show.
     */
    void showLicensingAlert(String message);

    /**
     * Shows a licensing alert.
     * @param message The message to show.
     * @param after The action to perform after the alert is dismissed.
     */
    void showLicensingAlert(String message,  @Nullable Runnable after);

    boolean showYesNoAlert(String title, String message);
}
