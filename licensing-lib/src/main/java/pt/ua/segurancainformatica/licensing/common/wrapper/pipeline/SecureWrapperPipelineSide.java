package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

public enum SecureWrapperPipelineSide {
    USER,
    MANAGER;

    public SecureWrapperPipelineSide opposite() {
        return this == USER ? MANAGER : USER;
    }
}
