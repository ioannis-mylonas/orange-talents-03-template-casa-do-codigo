package bootcamp.casacodigo.autor.form;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.validator.UniqueColumn;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AutorForm {
	@NotNull @NotBlank
	private String nome;
	@NotNull @NotBlank @Email @UniqueColumn(target=Autor.class, column="email")
	private String email;
	@NotNull @NotBlank @Size(max = 400)
	private String descricao;
	
	public AutorForm(@NotNull @NotBlank String nome,
			@NotNull @NotBlank @Email String email,
			@NotNull @NotBlank @Size(max = 400) String descricao) {
						
		this.nome = nome;
		this.email = email;
		this.descricao = descricao;
	}

	public Autor converte() {
		return new Autor(nome, email, descricao);
	}
}
