package bootcamp.casacodigo.autor.form;

import bootcamp.casacodigo.autor.model.Autor;

public class AutorBuilder {
    private String nome;
    private String email;
    private String descricao;

    public AutorBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public AutorBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AutorBuilder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public Autor build() {
        return new Autor(nome, email, descricao);
    }
}
