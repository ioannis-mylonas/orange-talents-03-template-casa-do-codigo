package bootcamp.casacodigo.livro.controller;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.livro.model.Livro;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LivroBuilder {
    private String titulo, resumo, sumario, isbn;
    private BigDecimal preco;
    private Integer paginas;
    private LocalDate publicacao;
    private Categoria categoria;
    private Autor autor;

    public LivroBuilder setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public LivroBuilder setResumo(String resumo) {
        this.resumo = resumo;
        return this;
    }

    public LivroBuilder setSumario(String sumario) {
        this.sumario = sumario;
        return this;
    }

    public LivroBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public LivroBuilder setPreco(BigDecimal preco) {
        this.preco = preco;
        return this;
    }

    public LivroBuilder setPaginas(Integer paginas) {
        this.paginas = paginas;
        return this;
    }

    public LivroBuilder setPublicacao(LocalDate publicacao) {
        this.publicacao = publicacao;
        return this;
    }

    public LivroBuilder setCategoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public LivroBuilder setAutor(Autor autor) {
        this.autor = autor;
        return this;
    }

    public Livro build() {
        return new Livro(titulo, resumo, sumario, preco, paginas, isbn, publicacao, categoria, autor);
    }
}
