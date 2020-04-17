package ml.socshared.adapter.vk.handler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
public class ErrorControllerHandler implements ErrorController {

    private static final String PATH = "/error";

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestApiError error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String path = (String) request.getAttribute("javax.servlet.error.request_uri");
        String message = (String) request.getAttribute("javax.servlet.error.message");

        RestApiError error = new RestApiError();
        error.setError(HttpStatus.valueOf(statusCode));
        error.setMessage(exception.getMessage());
        error.setPath(path);
        error.setStatus(statusCode);
        error.setTimestamp(LocalDateTime.now());

        return error;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
