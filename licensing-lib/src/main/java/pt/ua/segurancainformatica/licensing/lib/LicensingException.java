package pt.ua.segurancainformatica.licensing.lib;

import org.jetbrains.annotations.NotNull;

public class LicensingException extends Exception {
    public LicensingException(String message) {
        super(message);
    }

    public LicensingException(String message, Throwable cause) {
        super(message, cause);
    }

    @NotNull
    @Override
    public String getMessage() {
        var message = super.getMessage();
        return message == null ? "Licensing Error" : message;
    }
}
