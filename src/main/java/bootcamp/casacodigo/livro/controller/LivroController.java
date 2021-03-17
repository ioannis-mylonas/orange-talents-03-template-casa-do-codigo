package bootcamp.casacodigo.livro.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.form.LivroForm;
import bootcamp.casacodigo.livro.repository.LivroRepository;

@RestController
@RequestMapping("livros")
public class LivroController {

	private AutorRepository autorRepository;
	private CategoriaRepository categoriaRepository;
	private LivroRepository livroRepository;
	
	public LivroController(AutorRepository autorRepository,
			CategoriaRepository categoriaRepository,
			LivroRepository livroRepository) {
		
		this.autorRepository = autorRepository;
		this.categoriaRepository = categoriaRepository;
		this.livroRepository = livroRepository;
	}
	
	@PostMapping
	@Transactional
	public void cadastra(@RequestBody @Valid LivroForm form) {
		livroRepository.save(form.converte(autorRepository, categoriaRepository));
	}
}
