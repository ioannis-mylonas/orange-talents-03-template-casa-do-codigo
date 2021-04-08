package bootcamp.casacodigo.categoria.form;

import bootcamp.casacodigo.categoria.model.Categoria;

public class CategoriaBuilder {
    private String nome;

    public CategoriaBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public Categoria build() {
        return new Categoria(nome);
    }
}
