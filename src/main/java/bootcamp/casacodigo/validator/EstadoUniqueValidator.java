package bootcamp.casacodigo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import bootcamp.casacodigo.localizacao.form.EstadoForm;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;

public class EstadoUniqueValidator implements ConstraintValidator<EstadoUnique, EstadoForm> {

	private final EstadoRepository estadoRepository;
	
	public EstadoUniqueValidator(EstadoRepository estadoRepository) {
		this.estadoRepository = estadoRepository;
	}

	@Override
	public boolean isValid(EstadoForm value, ConstraintValidatorContext context) {
		return estadoRepository
				.findByNomeAndPais_Id(value.getNome(), value.getPaisId())
				.isEmpty();
	}

}
