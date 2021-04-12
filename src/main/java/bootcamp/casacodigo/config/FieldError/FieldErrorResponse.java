package bootcamp.casacodigo.config.FieldError;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;

public class FieldErrorResponse {
    private String message;
    private String field;

    public FieldErrorResponse(FieldError error, MessageSource messageSource) {
        this.message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
        this.field = error.getField();
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}
