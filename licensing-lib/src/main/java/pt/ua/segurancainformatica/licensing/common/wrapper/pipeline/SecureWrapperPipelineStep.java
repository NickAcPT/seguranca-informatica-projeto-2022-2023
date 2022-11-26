package pt.ua.segurancainformatica.licensing.common.wrapper.pipeline;

/**
 * A pipeline step that can be used to wrap and unwrap objects securely.
 * @param <I> The input type of the step.
 * @param <O> The output type of the step.
 */
public interface SecureWrapperPipelineStep<I, O> {

    /**
     * Wraps an object securely.
     * @param context The context of the pipeline.
     * @param input The object to wrap.
     * @return The wrapped object.
     */
    O wrap(SecureWrapperPipelineContext context, I input);

    /**
     * Unwraps an object securely.
     * @param context The context of the pipeline.
     * @param input The object to unwrap.
     * @return The unwrapped object.
     */
    I unwrap(SecureWrapperPipelineContext context, O input);

}
