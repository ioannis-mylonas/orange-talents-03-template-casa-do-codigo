package bootcamp.casacodigo.cliente.form;

public class ClienteFormBuilder {
    private String nome, sobrenome, email, documento, endereco, complemento, cidade, telefone, cep;
    private Long paisId, estadoId;

    public ClienteFormBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ClienteFormBuilder setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
        return this;
    }

    public ClienteFormBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public ClienteFormBuilder setDocumento(String documento) {
        this.documento = documento;
        return this;
    }

    public ClienteFormBuilder setEndereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public ClienteFormBuilder setComplemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public ClienteFormBuilder setCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public ClienteFormBuilder setTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public ClienteFormBuilder setCep(String cep) {
        this.cep = cep;
        return this;
    }

    public ClienteFormBuilder setPaisId(Long paisId) {
        this.paisId = paisId;
        return this;
    }

    public ClienteFormBuilder setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
        return this;
    }

    public ClienteForm build() {
        return new ClienteForm(nome, sobrenome, email,
                documento, endereco, complemento,
                cidade, paisId, estadoId, telefone, cep);
    }
}
