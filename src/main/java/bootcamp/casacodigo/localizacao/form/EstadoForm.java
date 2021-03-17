package bootcamp.casacodigo.localizacao.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import bootcamp.casacodigo.validator.OneExists;
import bootcamp.casacodigo.validator.UniqueColumn;

public class EstadoForm {
	@NotNull @NotBlank @UniqueColumn(target = Estado.class, column = "nome")
	private String nome;
	@NotNull @OneExists(target = Pais.class, column = "id")
	private Long paisId;
	
	public EstadoForm(String nome, Long paisId) {
		this.nome = nome;
		this.paisId = paisId;
	}
	
	public Estado converte(PaisRepository paisRepository) {
		Pais pais = paisRepository.getOne(paisId);
		return new Estado(nome, pais);
	}
}
