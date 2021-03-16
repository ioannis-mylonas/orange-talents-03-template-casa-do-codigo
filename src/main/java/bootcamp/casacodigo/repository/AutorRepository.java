package bootcamp.casacodigo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	Optional<Autor> findByEmail(String email);
}
