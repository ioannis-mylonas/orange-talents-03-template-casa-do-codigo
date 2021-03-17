package bootcamp.casacodigo.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bootcamp.casacodigo.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
