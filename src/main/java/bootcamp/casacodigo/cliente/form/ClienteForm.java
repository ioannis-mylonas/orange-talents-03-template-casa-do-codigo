package bootcamp.casacodigo.cliente.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bootcamp.casacodigo.cliente.model.Cliente;
import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import bootcamp.casacodigo.validator.CpfCnpj;
import bootcamp.casacodigo.validator.EstadoValido;
import bootcamp.casacodigo.validator.OneExists;
import bootcamp.casacodigo.validator.UniqueColumn;

@EstadoValido
public class ClienteForm {
	@NotBlank
	private String nome;
	@NotBlank
	private String sobrenome;
	@NotBlank @Email @UniqueColumn(target = Cliente.class, column = "email")
	private String email;
	@NotBlank @CpfCnpj
	@UniqueColumn(target = Cliente.class, column = "documento")
	private String documento;
	@NotBlank
	private String endereco;
	@NotBlank
	private String complemento;
	@NotBlank
	private String cidade;
	@NotNull @OneExists(target = Pais.class, column = "id")
	private Long paisId;
	private Long estadoId;
	@NotBlank
	private String telefone;
	@NotBlank
	private String cep;
	
	public ClienteForm(@NotNull @NotBlank String nome, @NotNull @NotBlank String sobrenome,
			@NotNull @NotBlank @Email String email, @NotNull @NotBlank String documento,
			@NotNull @NotBlank String endereco, @NotNull @NotBlank String complemento, @NotNull @NotBlank String cidade,
			@NotNull Long paisId, Long estadoId, @NotNull @NotBlank String telefone,
			@NotNull @NotBlank String cep) {
		
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.documento = documento;
		this.endereco = endereco;
		this.complemento = complemento;
		this.cidade = cidade;
		this.paisId = paisId;
		this.estadoId = estadoId;
		this.telefone = telefone;
		this.cep = cep;
	}
	
	public Long getPaisId() {
		return paisId;
	}
	
	public Long getEstadoId() {
		return estadoId;
	}

	public Cliente converte(EstadoRepository estadoRepository,
							PaisRepository paisRepository) {
		
		Pais pais = paisRepository.getOne(paisId);
		Estado estado = null;
		
		if (estadoId != null) estado = estadoRepository.getOne(estadoId);
		
		return new Cliente(nome, sobrenome, email, documento,
				endereco, complemento, cidade, estado, pais,
				telefone, cep);
	}	
}
