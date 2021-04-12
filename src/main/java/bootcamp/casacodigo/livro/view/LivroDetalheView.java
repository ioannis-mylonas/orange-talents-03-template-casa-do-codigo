package bootcamp.casacodigo.livro.view;

import bootcamp.casacodigo.autor.view.AutorLivroView;

import java.math.BigDecimal;

public class LivroDetalheView {
	String titulo, resumo, sumario, isbn;
	BigDecimal preco;
	Integer paginas;
	AutorLivroView autor;

	public LivroDetalheView(String titulo, String resumo, String sumario,
							String isbn, BigDecimal preco, Integer paginas,
							AutorLivroView autor) {

		this.titulo = titulo;
		this.resumo = resumo;
		this.sumario = sumario;
		this.isbn = isbn;
		this.preco = preco;
		this.paginas = paginas;
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getResumo() {
		return resumo;
	}

	public String getSumario() {
		return sumario;
	}

	public String getIsbn() {
		return isbn;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public Integer getPaginas() {
		return paginas;
	}

	public AutorLivroView getAutor() {
		return autor;
	}
}
