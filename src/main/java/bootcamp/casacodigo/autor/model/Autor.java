package bootcamp.casacodigo.autor.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Autor {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false, length = 400)
	private String descricao;
	@Column(nullable = false)
	private LocalDateTime criacao = LocalDateTime.now();
	
	@Deprecated
	public Autor() {}
		
	public Autor(String nome, String email, String descricao) {
		this.nome = nome;
		this.email = email.toLowerCase();
		this.descricao = descricao;
	}
	
	public Long getId() {
		return id;
	}
}
