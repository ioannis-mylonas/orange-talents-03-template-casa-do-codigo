package bootcamp.casacodigo.cliente.repository;

import bootcamp.casacodigo.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
