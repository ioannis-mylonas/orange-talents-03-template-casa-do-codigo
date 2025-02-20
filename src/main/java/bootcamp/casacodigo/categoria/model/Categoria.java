package bootcamp.casacodigo.categoria.model;

import javax.persistence.*;

@Entity
public class Categoria {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String nome;
	
	@Deprecated
	public Categoria() {}
	
	public Categoria(String nome) {
		this.nome = nome.toLowerCase();
	}
	
	public Long getId() {
		return id;
	}
}
