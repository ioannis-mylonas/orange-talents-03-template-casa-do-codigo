package bootcamp.casacodigo.livro.repository;

import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.livro.view.LivroDetalheView;
import bootcamp.casacodigo.livro.view.LivroSimplesView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
	/**
	 * Lista os livros conforme a projeção. Quando quiser listar a
	 * projeção, usar esse método no lugar de findAll()
	 * @return Lista de projeções de livros cadastrados
	 */
	public List<LivroSimplesView> findLivroSimplesBy();
	
	public Optional<LivroDetalheView> findLivroDetalheById(Long id);
}
