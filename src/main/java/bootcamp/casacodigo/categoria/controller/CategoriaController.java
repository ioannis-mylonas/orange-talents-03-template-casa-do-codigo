package bootcamp.casacodigo.categoria.controller;

import bootcamp.casacodigo.categoria.form.CategoriaForm;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	private final CategoriaRepository repository;
	
	public CategoriaController(CategoriaRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid CategoriaForm form) {
		Categoria categoria = form.converte();
		repository.save(categoria);
		return categoria.getId();
	}
}
