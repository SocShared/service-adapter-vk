package ml.socshared.adapter.vk.vkclient.exception;

import lombok.Data;
import ml.socshared.adapter.vk.vkclient.domain.ErrorType;

@Data
public class VKClientException extends Exception {
    public VKClientException(String msg, ErrorType errorType) {
        super(msg);
        this.errorType = errorType;
    }
    public VKClientException(ErrorType errorType) {
        super("vk is returned error object -> " + errorType.toString());
        this.errorType = errorType;
    }


    public ErrorType getErrorType() {
        return errorType;
    }

    private ErrorType errorType;
}
