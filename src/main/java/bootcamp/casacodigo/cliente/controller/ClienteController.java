package bootcamp.casacodigo.cliente.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.cliente.form.ClienteForm;
import bootcamp.casacodigo.cliente.model.Cliente;
import bootcamp.casacodigo.cliente.repository.ClienteRepository;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	private final EstadoRepository estadoRepository;
	private final PaisRepository paisRepository;
	private final ClienteRepository clienteRepository;
	
	public ClienteController(EstadoRepository estadoRepository,
			PaisRepository paisRepository,
			ClienteRepository clienteRepository) {
		
		this.estadoRepository = estadoRepository;
		this.paisRepository = paisRepository;
		this.clienteRepository = clienteRepository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid ClienteForm form) {
		Cliente cliente = form.converte(estadoRepository, paisRepository);
		clienteRepository.save(cliente);
		return cliente.getId();
	}
}
