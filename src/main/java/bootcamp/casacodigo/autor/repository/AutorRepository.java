package bootcamp.casacodigo.autor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.autor.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	Optional<Autor> findByEmail(String email);
}
