package bootcamp.casacodigo.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.form.AutorForm;
import bootcamp.casacodigo.repository.AutorRepository;

@RestController
@RequestMapping("/autores")
public class AutorController {
	
	private final AutorRepository repository;
	
	public AutorController(AutorRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping
	@Transactional
	public void cadastra(@RequestBody @Valid AutorForm autor) {
		repository.save(autor.converte());
	}
	
}
