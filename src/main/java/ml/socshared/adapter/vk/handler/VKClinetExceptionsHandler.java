package ml.socshared.adapter.vk.handler;


import javassist.NotFoundException;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class VKClinetExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VKClientException.class)
    protected ResponseEntity<String> handleVkClientExceptions() {
        return new ResponseEntity<>("VK ERROR", HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> handleNotFoundExceptions() {
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }


}
