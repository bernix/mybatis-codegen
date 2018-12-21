package cn.bning.codegen.exception;

/**
 * @author Bernix Ning
 * @date 2018-12-14
 */
public class GeneratorException extends RuntimeException {
    public GeneratorException() {
    }

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorException(Throwable cause) {
        super(cause);
    }
}
