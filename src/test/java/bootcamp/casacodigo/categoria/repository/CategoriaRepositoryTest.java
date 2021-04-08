package bootcamp.casacodigo.categoria.repository;

import bootcamp.casacodigo.categoria.model.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public static List<Categoria> geraCategorias() {
        return List.of(
                new Categoria("Ação"),
                new Categoria("Aventura"),
                new Categoria("Mistério"),
                new Categoria("Ficção Científica")
        );
    }

    public static Stream<Arguments> provideNomesExistentes() {
        List<Categoria> categorias = geraCategorias();

        return Stream.of(
                Arguments.of(categorias, "ação"),
                Arguments.of(categorias, "AVENTURA"),
                Arguments.of(categorias, "misTÉriO"),
                Arguments.of(categorias, "Ficção Científica")
        );
    }

    public static Stream<Arguments> provideNomesInexistentes() {
        List<Categoria> categorias = geraCategorias();

        return Stream.of(
                Arguments.of(categorias, "Comédia"),
                Arguments.of(categorias, "terror"),
                Arguments.of(categorias, "NULL")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNomesExistentes")
    public void testaFindNomesExistentes(List<Categoria> categorias, String nome) {
        categoriaRepository.saveAll(categorias);
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertTrue(categoria.isPresent());
    }

    @ParameterizedTest
    @MethodSource("provideNomesInexistentes")
    public void testaFindNomesInexistentes(List<Categoria> categorias, String nome) {
        categoriaRepository.saveAll(categorias);
        Optional<Categoria> categoria = categoriaRepository.findByNomeIgnoreCase(nome);
        Assertions.assertFalse(categoria.isPresent());
    }
}