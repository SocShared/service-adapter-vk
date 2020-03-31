package ml.socshared.adapter.vk.vkclient.exception;

import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.ErrorCode;

public class InvalidParameterException extends VKClientException {
    public InvalidParameterException(String msg) {
        super(msg, new Error(ErrorCode.INVALID_ARGUMENTS));
    }
}
