package bootcamp.casacodigo.localizacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.localizacao.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
