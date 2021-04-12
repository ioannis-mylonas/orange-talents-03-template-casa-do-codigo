package bootcamp.casacodigo.livro.model;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.categoria.model.Categoria;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Livro {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String titulo;
	@Column(nullable = false, length = 500)
	private String resumo;
	@Lob
	@Column(nullable = false)
	private String sumario;
	@Column(nullable = false)
	private BigDecimal preco;
	@Column(nullable = false)
	private Integer paginas;
	@Column(unique = true, nullable = false)
	private String isbn;
	@Column(nullable = false)
	private LocalDate publicacao;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Categoria categoria;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Autor autor;
	
	public Livro(String titulo, String resumo, String sumario,
			BigDecimal preco, Integer paginas, String isbn,
			LocalDate publicacao, Categoria categoria, Autor autor) {
		
		this.titulo = titulo;
		this.resumo = resumo;
		this.sumario = sumario;
		this.preco = preco;
		this.paginas = paginas;
		this.isbn = isbn;
		this.publicacao = publicacao;
		this.categoria = categoria;
		this.autor = autor;
	}
	
	public Long getId() {
		return id;
	}
}
