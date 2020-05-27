package ml.socshared.adapter.vk.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ml.socshared.template.exception.SocsharedErrors;

@Getter
public abstract class AbstractRestHandleableException extends RuntimeException implements ml.socshared.template.exception.HttpStatusCodeContainer {
    private static final long serialVersionUID = -3416823984750319182L;

    private HttpStatus httpStatus;
    private SocsharedErrors errorCode;

    public AbstractRestHandleableException(String message, SocsharedErrors errorCode, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public AbstractRestHandleableException(SocsharedErrors errorCode, HttpStatus httpStatus) {
        this(errorCode.getMessage(), errorCode, httpStatus);
    }

    public AbstractRestHandleableException(SocsharedErrors errorCode, Throwable throwable, HttpStatus httpStatus) {
        super(errorCode.getMessage(), throwable);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
