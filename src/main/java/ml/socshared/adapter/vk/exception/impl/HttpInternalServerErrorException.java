package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import ml.socshared.template.exception.SocsharedErrors;
import org.springframework.http.HttpStatus;

public class HttpInternalServerErrorException extends AbstractRestHandleableException {
    public HttpInternalServerErrorException() {
        super(SocsharedErrors.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public HttpInternalServerErrorException(SocsharedErrors errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    public HttpInternalServerErrorException(String message) {
        super(message, SocsharedErrors.INTERNAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
