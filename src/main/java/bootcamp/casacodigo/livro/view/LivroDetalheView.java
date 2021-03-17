package bootcamp.casacodigo.livro.view;

import java.math.BigDecimal;

import bootcamp.casacodigo.autor.view.AutorLivroView;

public interface LivroDetalheView {
	String getTitulo();
	String getResumo();
	String getSumario();
	BigDecimal getPreco();
	Integer getPaginas();
	String getIsbn();
	AutorLivroView getAutor();
}
