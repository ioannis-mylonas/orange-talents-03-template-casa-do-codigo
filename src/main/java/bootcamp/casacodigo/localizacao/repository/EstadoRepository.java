package bootcamp.casacodigo.localizacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	List<Estado> findByPais(Pais pais);
}
