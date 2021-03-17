package bootcamp.casacodigo.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import bootcamp.casacodigo.localizacao.form.EstadoForm;
import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;

public class EstadoUniqueValidator implements ConstraintValidator<EstadoUnique, EstadoForm> {

	private final PaisRepository paisRepository;
	private final EstadoRepository estadoRepository;
	
	public EstadoUniqueValidator(PaisRepository paisRepository, EstadoRepository estadoRepository) {
		this.paisRepository = paisRepository;
		this.estadoRepository = estadoRepository;
	}

	@Override
	public boolean isValid(EstadoForm value, ConstraintValidatorContext context) {
		Optional<Pais> pais = paisRepository.findById(value.getPaisId());
		if (pais.isPresent()) {
			Optional<Estado> estado = estadoRepository.findByNomeAndPais(value.getNome(), pais.get());
			return estado.isEmpty();
		}
		
		return false;
	}

}
