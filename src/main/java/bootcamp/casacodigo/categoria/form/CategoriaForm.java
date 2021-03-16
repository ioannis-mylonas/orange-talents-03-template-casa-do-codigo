package bootcamp.casacodigo.categoria.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.validator.CategoriaNomeUnique;

public class CategoriaForm {
	@NotNull @NotBlank @CategoriaNomeUnique
	private String nome;
	
	@Deprecated
	public CategoriaForm() {}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Categoria converte() {
		return new Categoria(nome);
	}
}
