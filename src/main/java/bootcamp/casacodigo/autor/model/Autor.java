package bootcamp.casacodigo.autor.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
		this.email = email;
		this.descricao = descricao;
	}
}
