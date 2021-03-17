package bootcamp.casacodigo.localizacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pais {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String nome;
	
	@Deprecated
	public Pais() {}
	
	public Pais(String nome) {
		this.nome = nome;
	}
}
