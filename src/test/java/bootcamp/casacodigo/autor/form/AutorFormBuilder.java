package bootcamp.casacodigo.autor.form;

public class AutorFormBuilder {
    private String nome;
    private String email;
    private String descricao;

    public AutorFormBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public AutorFormBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AutorFormBuilder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public AutorForm build() {
        return new AutorForm(nome, email, descricao);
    }
}
