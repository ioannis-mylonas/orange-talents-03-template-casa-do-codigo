package bootcamp.casacodigo.validator;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@ConstraintComposition(CompositionType.OR)
@CPF @CNPJ
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfCnpj {
	String message() default "CPF/CNPJ em formato inválido";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
