package bootcamp.casacodigo.livro.form;

import java.math.BigDecimal;
import java.time.LocalDate;

class LivroFormBuilder {
    private String titulo, resumo, sumario, isbn;
    private BigDecimal preco;
    private Integer paginas;
    private LocalDate publicacao;
    private Long categoriaId, autorId;

    public LivroFormBuilder setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public LivroFormBuilder setResumo(String resumo) {
        this.resumo = resumo;
        return this;
    }

    public LivroFormBuilder setSumario(String sumario) {
        this.sumario = sumario;
        return this;
    }

    public LivroFormBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public LivroFormBuilder setPreco(BigDecimal preco) {
        this.preco = preco;
        return this;
    }

    public LivroFormBuilder setPaginas(Integer paginas) {
        this.paginas = paginas;
        return this;
    }

    public LivroFormBuilder setPublicacao(LocalDate publicacao) {
        this.publicacao = publicacao;
        return this;
    }

    public LivroFormBuilder setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
        return this;
    }

    public LivroFormBuilder setAutorId(Long autorId) {
        this.autorId = autorId;
        return this;
    }

    public LivroForm build() {
        LivroForm form = new LivroForm(titulo, resumo, sumario,
                preco, paginas, isbn, categoriaId, autorId);
        form.setPublicacao(publicacao);

        return form;
    }
}