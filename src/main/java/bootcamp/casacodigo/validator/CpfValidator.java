package bootcamp.casacodigo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value.matches("^(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}|\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2})$");
	}
	
}
