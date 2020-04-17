package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import ml.socshared.adapter.vk.exception.AswErrors;
import org.springframework.http.HttpStatus;

public class HttpNotFoundException extends AbstractRestHandleableException {
    public HttpNotFoundException() {
        super(AswErrors.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public HttpNotFoundException(AswErrors errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    public HttpNotFoundException(String message) {
        super(message, AswErrors.NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
