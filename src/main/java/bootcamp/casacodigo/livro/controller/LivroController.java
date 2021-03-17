package bootcamp.casacodigo.livro.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.form.LivroForm;
import bootcamp.casacodigo.livro.repository.LivroRepository;
import bootcamp.casacodigo.livro.view.LivroDetalheView;
import bootcamp.casacodigo.livro.view.LivroSimplesView;

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
	
	@GetMapping
	public List<LivroSimplesView> lista() {
		return livroRepository.findLivroSimplesBy();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<LivroDetalheView> detalhe(@PathVariable Long id) {
		Optional<LivroDetalheView> livro = livroRepository.findLivroDetalheById(id);
		if (livro.isPresent()) {
			return ResponseEntity.ok(livro.get());
		}
		
		return ResponseEntity.notFound().build();
	}
}
