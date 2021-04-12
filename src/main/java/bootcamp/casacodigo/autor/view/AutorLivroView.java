package bootcamp.casacodigo.autor.view;

import bootcamp.casacodigo.autor.model.Autor;

public class AutorLivroView {
	private String nome, descricao;

	public AutorLivroView(Autor autor) {
		this.nome = autor.getNome();
		this.descricao = autor.getDescricao();
	}

	public AutorLivroView(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
}
