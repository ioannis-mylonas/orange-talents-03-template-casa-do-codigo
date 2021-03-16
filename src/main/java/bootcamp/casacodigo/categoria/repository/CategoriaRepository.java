package bootcamp.casacodigo.categoria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.categoria.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	Optional<Categoria> findByNome(String nome);
}
