package bootcamp.casacodigo.autor.view;

public class AutorLivroView {
	private String nome, descricao;

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
