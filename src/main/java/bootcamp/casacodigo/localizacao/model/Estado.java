package bootcamp.casacodigo.localizacao.model;

import javax.persistence.*;

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
	
	public Long getId() {
		return id;
	}

	public Pais getPais() {
		return pais;
	}
}
