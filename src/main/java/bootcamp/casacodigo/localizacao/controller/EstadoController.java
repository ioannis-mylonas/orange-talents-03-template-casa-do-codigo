package bootcamp.casacodigo.localizacao.controller;

import bootcamp.casacodigo.localizacao.form.EstadoForm;
import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	private EstadoRepository estadoRepository;
	private PaisRepository paisRepository;
	
	public EstadoController(EstadoRepository estadoRepository,
			PaisRepository paisRepository) {
		this.estadoRepository = estadoRepository;
		this.paisRepository = paisRepository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid EstadoForm form) {
		Estado res = form.converte(paisRepository);
		estadoRepository.save(res);
		return res.getId();
	}
}
