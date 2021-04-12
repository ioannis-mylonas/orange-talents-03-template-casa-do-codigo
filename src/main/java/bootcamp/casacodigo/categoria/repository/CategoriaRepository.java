package bootcamp.casacodigo.categoria.repository;

import bootcamp.casacodigo.categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	Optional<Categoria> findByNomeIgnoreCase(String nome);
}
