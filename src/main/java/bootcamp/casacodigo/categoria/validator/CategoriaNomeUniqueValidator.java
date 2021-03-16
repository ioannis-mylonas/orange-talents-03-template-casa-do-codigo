package bootcamp.casacodigo.categoria.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import bootcamp.casacodigo.categoria.repository.CategoriaRepository;

public class CategoriaNomeUniqueValidator implements
	ConstraintValidator<CategoriaNomeUnique, String> {
	
	private final CategoriaRepository repository;
	
	public CategoriaNomeUniqueValidator(CategoriaRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !repository.findByNome(value).isPresent();
	}
}
