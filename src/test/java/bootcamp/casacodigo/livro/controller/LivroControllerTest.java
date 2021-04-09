package bootcamp.casacodigo.livro.controller;

import bootcamp.casacodigo.autor.form.AutorBuilder;
import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.livro.repository.LivroRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(LivroController.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class LivroControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private LivroRepository livroRepository;

    @BeforeAll
    public void setup () {
        Categoria biografia = new Categoria("Biografia");
        Categoria didatico = new Categoria("Didático");

        List<Categoria> categorias = List.of(
                new Categoria("Ação"),
                new Categoria("Mistério"),
                new Categoria("Aventura"),
                new Categoria("Romance")
        );
        categoriaRepository.saveAll(categorias);

        AutorBuilder autorBuilder = new AutorBuilder();

        Autor joaquim = autorBuilder
                .setNome("Joao Joaquim")
                .setEmail("joao.joaquim@email.com")
                .setDescricao("Dr. João Joaquim").build();

        Autor mariana = autorBuilder
                .setNome("Mariana da Silva")
                .setEmail("mariana.silva@email.com")
                .setDescricao("Dra. Mariana da Silva").build();

        List<Autor> autores = List.of(joaquim, mariana);
        autorRepository.saveAll(autores);

        LivroBuilder livroBuilder = new LivroBuilder();
        List<Livro> livros = List.of(
                livroBuilder
                        .setTitulo("O Vôo da Galinha")
                        .setResumo("Um livro emocionante.")
                        .setSumario("Um sumário qualquer")
                        .setIsbn("O Vôo da Galinha")
                        .setPreco(BigDecimal.valueOf(25.35))
                        .setPaginas(152)
                        .setPublicacao(LocalDate.of(2022, 5, 14))
                        .setAutor(joaquim)
                        .setCategoria(biografia).build(),
                livroBuilder
                        .setTitulo("O Grade Balão")
                        .setResumo("Um grande livro emocionante.")
                        .setSumario("Um sumário qualquer")
                        .setIsbn("O Grade Balão")
                        .setPreco(BigDecimal.valueOf(28.90))
                        .setPaginas(200)
                        .setPublicacao(LocalDate.of(2025, 2, 21))
                        .setAutor(mariana)
                        .setCategoria(didatico).build()
        );
        livroRepository.saveAll(livros);
    }
}