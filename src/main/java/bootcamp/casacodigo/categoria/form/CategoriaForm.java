package bootcamp.casacodigo.categoria.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.validator.UniqueColumn;

public class CategoriaForm {
	@NotNull @NotBlank @UniqueColumn(target=Categoria.class, column="nome")
	private String nome;
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Categoria converte() {
		return new Categoria(nome);
	}
}
