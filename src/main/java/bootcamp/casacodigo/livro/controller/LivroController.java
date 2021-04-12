package bootcamp.casacodigo.livro.controller;

import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.form.LivroForm;
import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.livro.repository.LivroRepository;
import bootcamp.casacodigo.livro.view.LivroDetalheView;
import bootcamp.casacodigo.livro.view.LivroSimplesView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
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
	public Long cadastra(@RequestBody @Valid LivroForm form) {
		Livro livro = form.converte(autorRepository, categoriaRepository);
		livroRepository.save(livro);
		return livro.getId();
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
