package bootcamp.casacodigo.localizacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Estado {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String nome;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Pais pais;
	
	@Deprecated
	public Estado() {}
	
	public Estado(String nome, Pais pais) {
		this.nome = nome;
		this.pais = pais;
	}
}
