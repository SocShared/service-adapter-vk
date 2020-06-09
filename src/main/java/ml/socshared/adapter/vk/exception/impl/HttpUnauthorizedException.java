package ml.socshared.adapter.vk.exception.impl;

import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import org.springframework.http.HttpStatus;

public class HttpUnauthorizedException extends AbstractRestHandleableException {
    public HttpUnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public HttpUnauthorizedException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public HttpUnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
