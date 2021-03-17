package bootcamp.casacodigo.livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.livro.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

}
