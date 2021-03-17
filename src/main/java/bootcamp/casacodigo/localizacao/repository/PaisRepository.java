package bootcamp.casacodigo.localizacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.localizacao.model.Pais;

public interface PaisRepository extends JpaRepository<Pais, Long> {

}
