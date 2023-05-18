package earth.terrarium.hermes.api;

public class TagParseException extends RuntimeException {

    public TagParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagParseException(String message) {
        super(message);
    }
}
