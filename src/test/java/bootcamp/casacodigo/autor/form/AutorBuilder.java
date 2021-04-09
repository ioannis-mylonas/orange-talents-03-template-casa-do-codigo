package bootcamp.casacodigo.autor.form;

import bootcamp.casacodigo.autor.model.Autor;

public class AutorBuilder {
    private String nome;
    private String email;
    private String descricao;

    public AutorBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public AutorBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public AutorBuilder setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public Autor build() {
        return new Autor(nome, email, descricao);
    }
}
