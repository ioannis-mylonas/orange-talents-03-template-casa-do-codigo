package bootcamp.casacodigo.config.FieldError;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FieldErrorHandler {
    private MessageSource messageSource;

    public FieldErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldErrorListResponse handle(MethodArgumentNotValidException ex) {

        return new FieldErrorListResponse(ex.getFieldErrors(), messageSource);
    }
}
