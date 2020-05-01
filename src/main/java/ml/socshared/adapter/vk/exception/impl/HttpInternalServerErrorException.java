package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import ml.socshared.adapter.vk.exception.AswErrors;
import org.springframework.http.HttpStatus;

public class HttpInternalServerErrorException extends AbstractRestHandleableException {
    public HttpInternalServerErrorException() {
        super(AswErrors.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public HttpInternalServerErrorException(AswErrors errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    public HttpInternalServerErrorException(String message) {
        super(message, AswErrors.INTERNAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
