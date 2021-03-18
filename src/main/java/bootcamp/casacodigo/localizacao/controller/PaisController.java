package bootcamp.casacodigo.localizacao.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.localizacao.form.PaisForm;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;

@RestController
@RequestMapping("/paises")
public class PaisController {
	
	private PaisRepository repository;
	
	public PaisController(PaisRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping
	@Transactional
	public Pais cadastra(@RequestBody @Valid PaisForm form) {
		Pais res = form.converte();
		repository.save(res);
		return res;
	}
}
