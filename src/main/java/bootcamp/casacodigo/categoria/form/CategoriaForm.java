package bootcamp.casacodigo.categoria.form;

import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.validator.UniqueColumn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
