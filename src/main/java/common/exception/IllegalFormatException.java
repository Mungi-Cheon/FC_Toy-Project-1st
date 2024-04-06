package common.exception;

public class IllegalFormatException extends RuntimeException {

    public IllegalFormatException(String msg) {
        super(msg);
    }

    public IllegalFormatException(String msg, String fileName) {
        super(String.format(msg, fileName));
    }
}
