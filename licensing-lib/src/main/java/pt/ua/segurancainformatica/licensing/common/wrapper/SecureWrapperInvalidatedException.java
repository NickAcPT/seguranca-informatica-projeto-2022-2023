package pt.ua.segurancainformatica.licensing.common.wrapper;

public class SecureWrapperInvalidatedException extends Exception {
    private final SecureWrapperInvalidatedReason reason;

    public SecureWrapperInvalidatedException(SecureWrapperInvalidatedReason reason) {
        super(reason.message());
        this.reason = reason;
    }

    public SecureWrapperInvalidatedException(SecureWrapperInvalidatedReason reason, Throwable cause) {
        super(reason.message(), cause);
        this.reason = reason;
    }

    public SecureWrapperInvalidatedReason reason() {
        return reason;
    }
}
