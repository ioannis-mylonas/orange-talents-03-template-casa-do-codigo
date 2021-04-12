package bootcamp.casacodigo.categoria.repository;

import bootcamp.casacodigo.categoria.model.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private List<Categoria> categorias = List.of(
            new Categoria("Ação"),
            new Categoria("Aventura"),
            new Categoria("Mistério"),
            new Categoria("Ficção Científica")
    );

    @BeforeEach
    public void setup() {
        categoriaRepository.saveAll(categorias);
    }

    @ParameterizedTest
    @CsvSource({"ação", "AVENTURA", "misTÉriO", "Ficção Científica"})
    public void testaFindNomesExistentes(String nome) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertTrue(categoria.isPresent(), String.format(
                "Categoria de nome %s foi salva e deveria estar presente!", nome
        ));
    }

    @ParameterizedTest
    @CsvSource({"Comédia", "terror", "NULL"})
    public void testaFindNomesInexistentes(String nome) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertFalse(categoria.isPresent(), String.format(
                "Categoria de nome %s não foi salva e não deveria estar presente!", nome
        ));
    }
}