package ml.socshared.adapter.vk.vkclient.exception;

import ml.socshared.adapter.vk.vkclient.domain.ErrorType;
import ml.socshared.adapter.vk.vkclient.domain.ErrorCode;

public class InvalidParameterException extends VKClientException {
    public InvalidParameterException(String msg) {
        super(msg, new ErrorType(ErrorCode.INVALID_ARGUMENTS));
    }
}
