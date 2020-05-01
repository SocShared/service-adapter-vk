package ml.socshared.adapter.vk.vkclient.exception;

import ml.socshared.adapter.vk.vkclient.domain.ErrorType;

public class VKClientException extends Exception {
    public VKClientException(String msg, ErrorType errorType) {
        super(msg);
        this.errorType = errorType;
    }
    public VKClientException(ErrorType errorType) {
        super("vk is returned error object");
        this.errorType = errorType;
    }


    public ErrorType getErrorType() {
        return errorType;
    }

    private ErrorType errorType;
}
