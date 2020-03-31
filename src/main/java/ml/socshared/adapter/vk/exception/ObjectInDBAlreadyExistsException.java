package ml.socshared.adapter.vk.exception;

public class ObjectInDBAlreadyExistsException extends RuntimeException {
    public ObjectInDBAlreadyExistsException(String msg) {
        super(msg);
    }
}
