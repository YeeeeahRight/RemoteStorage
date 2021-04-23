package by.bsuir.storage.exception;

public class OutOfStorageBoundsException extends RuntimeException {

    public OutOfStorageBoundsException() {
    }

    public OutOfStorageBoundsException(String message) {
        super(message);
    }
}
