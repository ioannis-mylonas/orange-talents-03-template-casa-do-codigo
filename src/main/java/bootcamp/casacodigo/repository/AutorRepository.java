package bootcamp.casacodigo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}
