package pt.ua.segurancainformatica.licensing.common.wrapper;

public enum SecureWrapperInvalidatedReason {
    ERROR_CIPHERING_SECURE_OBJECT("Error ciphering secure object."),
    ERROR_CIPHERING_SYMMETRIC_KEY("Error ciphering symmetric key."),
    ERROR_DECIPHERING_SECURE_OBJECT("Error deciphering secure object."),
    ERROR_DECIPHERING_SYMMETRIC_KEY("Error deciphering symmetric key."),
    ERROR_DESERIALIZE_OBJECT("Error deserializing object: Unable to deserialize the object from smile format."),
    ERROR_SERIALIZE_OBJECT("Error serializing object."),
    ERROR_SIGNING_SECURE_OBJECT("Error signing secure object."),
    ERROR_VERIFYING_SIGNATURE("Error verifying signature."),
    INVALID_OBJECT_SIGNATURE("Object signature is invalid."),
    PRIVATE_KEY_MISSING("Unable to decipher the symmetric key because the private key is missing."),
    NULL_CIPHER_KEY("Cipher key is null."),
    OBJECT_HAS_NO_SIGNATURE("Object has no signature."),
    PUBLIC_KEY_MISMATCH("Public key mismatch: Known public key is different from the one used to sign the object."),
    SYMMETRIC_KEY_CIPHERED_WITH_IV("Symmetric key ciphered with an IV."),
    USER_KEYPAIR_MISSING("User key pair is null."),
    KEYPAIR_MISSING("Key pair is null."),
    ;

    private final String message;

    SecureWrapperInvalidatedReason(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
