package pt.ua.segurancainformatica.licensing.lib;

public class LicensingException extends Exception {
    public LicensingException(String message) {
        super(message);
    }

    public LicensingException(String message, Throwable cause) {
        super(message, cause);
    }
}
