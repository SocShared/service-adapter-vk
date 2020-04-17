package ml.socshared.adapter.vk.exception.impl;


import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import ml.socshared.adapter.vk.exception.AswErrors;
import org.springframework.http.HttpStatus;

public class HttpBadRequestException extends AbstractRestHandleableException {
    public HttpBadRequestException() {
        super(AswErrors.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    public HttpBadRequestException(AswErrors errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    public HttpBadRequestException(String message) {
        super(message, AswErrors.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }
}
