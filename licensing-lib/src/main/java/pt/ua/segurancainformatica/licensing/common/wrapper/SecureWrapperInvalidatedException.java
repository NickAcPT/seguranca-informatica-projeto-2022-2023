package pt.ua.segurancainformatica.licensing.common.wrapper;

public class SecureWrapperInvalidatedException extends Exception {
    public SecureWrapperInvalidatedException(String message) {
        super(message);
    }

    public SecureWrapperInvalidatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
