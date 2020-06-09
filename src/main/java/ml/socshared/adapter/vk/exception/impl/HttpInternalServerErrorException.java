package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import org.springframework.http.HttpStatus;

public class HttpInternalServerErrorException extends AbstractRestHandleableException {
    public HttpInternalServerErrorException() {
        super(HttpStatus.NOT_FOUND);
    }

    public HttpInternalServerErrorException( HttpStatus httpStatus) {
        super(httpStatus);
    }

    public HttpInternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
