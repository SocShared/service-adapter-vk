package ml.socshared.adapter.vk.vkclient.exception;

import ml.socshared.adapter.vk.vkclient.domain.Error;

public class VKClientException extends Exception {
    public VKClientException(String msg, Error error) {
        super(msg);
        this.error = error;
    }
    public VKClientException(Error error) {
        super("vk is returned error object");
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    private Error error;
}
