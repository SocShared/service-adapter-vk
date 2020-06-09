package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import org.springframework.http.HttpStatus;

public class HttpForbiddenException extends AbstractRestHandleableException {
    public HttpForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }

    public HttpForbiddenException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public HttpForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
