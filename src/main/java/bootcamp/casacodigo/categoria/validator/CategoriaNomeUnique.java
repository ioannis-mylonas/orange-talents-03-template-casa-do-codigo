package bootcamp.casacodigo.categoria.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CategoriaNomeUniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoriaNomeUnique {
	String message() default "Categoria deve ter nome único";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
