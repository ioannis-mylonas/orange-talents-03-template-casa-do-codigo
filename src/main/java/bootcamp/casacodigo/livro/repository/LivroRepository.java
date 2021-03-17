package bootcamp.casacodigo.livro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.livro.view.LivroSimplesView;

public interface LivroRepository extends JpaRepository<Livro, Long> {
	/**
	 * Lista os livros conforme a projeção. Quando quiser listar a
	 * projeção, usar esse método no lugar de findAll()
	 * @return Lista de projeções de livros cadastrados
	 */
	public List<LivroSimplesView> findBy();
}
