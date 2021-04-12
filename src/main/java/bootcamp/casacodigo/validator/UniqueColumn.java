package bootcamp.casacodigo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueColumnValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueColumn {
	String message() default "Campo deve ser único";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	Class<?> target();
	String column();
}
