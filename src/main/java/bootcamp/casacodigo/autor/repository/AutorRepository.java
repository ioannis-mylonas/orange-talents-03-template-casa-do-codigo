package bootcamp.casacodigo.autor.repository;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.view.AutorLivroView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	Optional<Autor> findByEmailIgnoreCase(String email);
	AutorLivroView findAutorLivroViewById(Long id);
}
