package bootcamp.casacodigo.categoria.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.categoria.form.CategoriaForm;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	private final CategoriaRepository repository;
	
	public CategoriaController(CategoriaRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping
	@Transactional
	public void cadastra(@RequestBody @Valid CategoriaForm form) {
		repository.save(form.converte());
	}
}
