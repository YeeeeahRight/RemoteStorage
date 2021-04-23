package by.bsuir.storage.exception;

public class EntityNotFound extends RuntimeException {

    public EntityNotFound() {
    }

    public EntityNotFound(String message) {
        super(message);
    }
}
