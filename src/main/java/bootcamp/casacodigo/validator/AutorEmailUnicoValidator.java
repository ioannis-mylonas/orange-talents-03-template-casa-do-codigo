package bootcamp.casacodigo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import bootcamp.casacodigo.repository.AutorRepository;

@Component
public class AutorEmailUnicoValidator implements
	ConstraintValidator<AutorEmailUnico, String> {
	
	private final AutorRepository repository;
	
	public AutorEmailUnicoValidator(AutorRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void initialize(AutorEmailUnico constraint) {}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext ctx) {
		return !repository.findByEmail(email).isPresent();
	}
}
