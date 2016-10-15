package reader;

/**
 *
 * Represents a invalid NBT format
 */
public class InvalidNBTFormatException extends RuntimeException {

    public InvalidNBTFormatException() {
        super();
    }

    public InvalidNBTFormatException(String message) {
        super(message);
    }

    public InvalidNBTFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNBTFormatException(Throwable cause) {
        super(cause);
    }
}
