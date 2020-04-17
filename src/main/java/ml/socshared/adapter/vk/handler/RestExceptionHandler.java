package ml.socshared.adapter.vk.handler;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.exception.AbstractRestHandleableException;
import ml.socshared.adapter.vk.exception.AswErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<RestApiError> buildErrorResponse(Exception exc, HttpStatus httpStatus,
                                                            ServletWebRequest webRequest, AswErrors errorCode) {
        return new ResponseEntity<>(new RestApiError(exc, httpStatus, webRequest, errorCode), httpStatus);
    }

    @ExceptionHandler(AbstractRestHandleableException.class)
    public ResponseEntity<RestApiError> handlePrintException(ServletWebRequest webRequest, AbstractRestHandleableException exc) {
        log.error(exc.getMessage());
        return buildErrorResponse(exc, exc.getHttpStatus(), webRequest, exc.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiError> handlePrintException(ServletWebRequest webRequest, Exception exc) {
        log.error(exc.getMessage());
        return buildErrorResponse(exc, HttpStatus.INTERNAL_SERVER_ERROR, webRequest, AswErrors.INTERNAL);
    }
}

