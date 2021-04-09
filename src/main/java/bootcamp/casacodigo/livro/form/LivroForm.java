package bootcamp.casacodigo.livro.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.validator.OneExists;
import bootcamp.casacodigo.validator.UniqueColumn;

public class LivroForm {
	@NotBlank @UniqueColumn(target = Livro.class, column = "titulo")
	private String titulo;
	@NotBlank @Size(max = 500)
	private String resumo;
	@NotBlank
	private String sumario;
	@NotNull @Min(value = 20)
	private BigDecimal preco;
	@NotNull @Min(value = 100)
	private Integer paginas;
	@NotBlank @UniqueColumn(target = Livro.class, column = "isbn")
	private String isbn;
	@Future @NotNull
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate publicacao;
	@NotNull @OneExists(target = Categoria.class, column = "id")
	private Long categoriaId;
	@NotNull @OneExists(target = Autor.class, column = "id")
	private Long autorId;

	public LivroForm(@NotBlank String titulo,
					 @NotBlank @Size(max = 500) String resumo,
					 @NotBlank String sumario,
					 @NotNull @Min(value = 20) BigDecimal preco,
					 @NotNull @Min(value = 100) Integer paginas,
					 @NotBlank String isbn,
					 @NotNull Long categoriaId,
					 @NotNull Long autorId) {

		this.titulo = titulo;
		this.resumo = resumo;
		this.sumario = sumario;
		this.preco = preco;
		this.paginas = paginas;
		this.isbn = isbn;
		this.categoriaId = categoriaId;
		this.autorId = autorId;
	}

	/**
	 * Método setter criado para serialização do Jackson
	 * que não funciona com o construtor.
	 * @param publicacao Data futura
	 */
	public void setPublicacao(LocalDate publicacao) {
		this.publicacao = publicacao;
	}

	public Livro converte(AutorRepository autorRepository,
			CategoriaRepository categoriaRepository) {
		
		Autor autor = autorRepository.getOne(autorId);
		Categoria categoria = categoriaRepository.getOne(categoriaId);
		
		return new Livro(titulo, resumo, sumario,
				preco, paginas, isbn,
				publicacao, categoria, autor);
	}
}
