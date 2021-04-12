package bootcamp.casacodigo.cliente.model;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;

import javax.persistence.*;

@Entity
public class Cliente {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private String sobrenome;
	@Column(unique = true, nullable = false)
	private String documento;
	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false)
	private String complemento;
	@Column(nullable = false)
	private String cidade;
	@Column(nullable = false)
	private String telefone;
	@Column(nullable = false)
	private String cep;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Pais pais;
	@ManyToOne
	private Estado estado;
	
	public Cliente(String nome, String sobrenome, String email,
			String documento, String endereco, String complemento,
			String cidade, Estado estado, Pais pais, String telefone, String cep) {
		
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.endereco = endereco;
		this.complemento = complemento;
		this.cidade = cidade;
		this.estado = estado;
		this.pais = pais;
		this.telefone = telefone;
		this.cep = cep;

		this.documento = cleanDocumento(documento);
	}
	
	public Long getId() {
		return id;
	}

	public static String cleanDocumento(String documento) {
		if (documento != null) return documento.replaceAll("[.\\-/]", "");
		return null;
	}
}
