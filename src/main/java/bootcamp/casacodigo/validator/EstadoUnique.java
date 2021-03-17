package bootcamp.casacodigo.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EstadoUniqueValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EstadoUnique {
	String message() default "Deve ser único";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
