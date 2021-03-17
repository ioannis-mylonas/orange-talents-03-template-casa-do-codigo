package bootcamp.casacodigo.localizacao.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.validator.UniqueColumn;

public class PaisForm {
	@NotNull @NotBlank @UniqueColumn(target = Pais.class, column = "nome")
	private String nome;
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Pais converte() {
		return new Pais(nome);
	}
}
