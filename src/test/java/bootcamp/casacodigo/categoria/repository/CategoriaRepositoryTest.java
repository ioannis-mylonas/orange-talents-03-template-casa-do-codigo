package bootcamp.casacodigo.categoria.repository;

import bootcamp.casacodigo.categoria.model.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private List<Categoria> categorias = List.of(
            new Categoria("Ação"),
            new Categoria("Aventura"),
            new Categoria("Mistério"),
            new Categoria("Ficção Científica")
    );

    public static Stream<Arguments> provideNomesExistentes() {
        return Stream.of(
                Arguments.of("ação"),
                Arguments.of("AVENTURA"),
                Arguments.of("misTÉriO"),
                Arguments.of("Ficção Científica")
        );
    }

    public static Stream<Arguments> provideNomesInexistentes() {
        return Stream.of(
                Arguments.of("Comédia"),
                Arguments.of("terror"),
                Arguments.of("NULL")
        );
    }

    @BeforeAll
    @Transactional
    public void setupClass() {
        categoriaRepository.saveAll(categorias);
    }

    @ParameterizedTest
    @MethodSource("provideNomesExistentes")
    public void testaFindNomesExistentes(String nome) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertTrue(categoria.isPresent(), String.format(
                "Categoria de nome %s foi salva e deveria estar presente!", nome
        ));
    }

    @ParameterizedTest
    @MethodSource("provideNomesInexistentes")
    public void testaFindNomesInexistentes(String nome) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertFalse(categoria.isPresent(), String.format(
                "Categoria de nome %s não foi salva e não deveria estar presente!", nome
        ));
    }
}