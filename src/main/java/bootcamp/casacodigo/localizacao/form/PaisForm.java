package bootcamp.casacodigo.localizacao.form;

import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.validator.UniqueColumn;

import javax.validation.constraints.NotBlank;

public class PaisForm {
	@NotBlank @UniqueColumn(target = Pais.class, column = "nome")
	private String nome;
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Pais converte() {
		return new Pais(nome);
	}
}
