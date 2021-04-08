package bootcamp.casacodigo.categoria.form;

public class CategoriaFormBuilder {
    private String nome;

    public CategoriaFormBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public CategoriaForm build() {
        CategoriaForm form = new CategoriaForm();
        form.setNome(nome);

        return form;
    }
}
