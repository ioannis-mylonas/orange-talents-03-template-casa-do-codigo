package bootcamp.casacodigo.validator;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import bootcamp.casacodigo.cliente.form.ClienteForm;
import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;

public class EstadoValidoValidator implements ConstraintValidator<EstadoValido, ClienteForm> {

	private final PaisRepository paisRepository;
	private final EstadoRepository estadoRepository;
	
	public EstadoValidoValidator(PaisRepository paisRepository, EstadoRepository estadoRepository) {
		this.paisRepository = paisRepository;
		this.estadoRepository = estadoRepository;
	}

	@Override
	public boolean isValid(ClienteForm value, ConstraintValidatorContext context) {
		if (value.getPaisId() == null) return false;
		Optional<Pais> pais = paisRepository.findById(value.getPaisId());
		if (pais.isEmpty()) return false;
		
		List<Estado> estados = estadoRepository.findByPais(pais.get());
		if (!estados.isEmpty() && value.getEstadoId() == null) return false;
		else if (estados.isEmpty()) return true;
		
		for (Estado estado : estados) {
			if (estado.getId() == value.getEstadoId()) return true;
		}
		
		return false;
	}
	
}
