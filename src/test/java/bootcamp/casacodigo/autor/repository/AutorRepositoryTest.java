package bootcamp.casacodigo.autor.repository;

import bootcamp.casacodigo.autor.form.AutorBuilder;
import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.view.AutorLivroView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
class AutorRepositoryTest {
    @Autowired
    private AutorRepository autorRepository;

    public static List<Autor> geraAutoresValidos() {
        AutorBuilder builder = new AutorBuilder();

        return List.of(
                builder
                        .nome("Mariana Soares")
                        .email("mariana.soares@email.com")
                        .descricao("Uma Descricao").build(),
                builder
                        .nome("João Joaquim")
                        .email("joao.joaquim@email.com")
                        .descricao("Dr. João Joaquim").build(),
                builder
                        .nome("Ellionel dos Santos")
                        .email("ellionel.santos@email.com")
                        .descricao("Prof. Ellionel").build()
        );
    }

    public static Stream<Arguments> provideAutoresForConsultaEmail() {
        List<Autor> autores = geraAutoresValidos();

        List<String> emailsInexistentes = List.of(
                "karen.soares@email.com", "emailInvalido", "emailInvalido2.email",
                "Jonas Jonaquim", "jose.soares@email.com", "joão.joaquim@email.com"
        );

        List<String> emailsExistentes = List.of(
                "mariana.soares@email.com", "joao.joaquim@email.com",
                "ellionel.santos@email.com", "JoAo.JoAqUiM@email.com"
        );

        return Stream.of(Arguments.of(autores, emailsInexistentes, emailsExistentes));
    }

    public static Stream<Arguments> providenciaAutoresValidosParaTeste() {
        List<Autor> autores = geraAutoresValidos();
        return Stream.of(Arguments.of(autores));
    }

    @ParameterizedTest
    @MethodSource("provideAutoresForConsultaEmail")
    public void testaBuscaEmailExistente(List<Autor> autores,
                                         List<String> emailsInexistentes,
                                         List<String> emailsExistentes) {

        autorRepository.saveAll(autores);

        for (String email : emailsInexistentes) {
            Optional<Autor> autor = autorRepository.findByEmailIgnoreCase(email);
            Assertions.assertFalse(autor.isPresent());
        }

        for (String email : emailsExistentes) {
            Optional<Autor> autor = autorRepository.findByEmailIgnoreCase(email);
            Assertions.assertTrue(autor.isPresent());
        }
    }

    @ParameterizedTest
    @MethodSource("providenciaAutoresValidosParaTeste")
    public void testaBuscaAutorLivroView(List<Autor> autores) {
        autorRepository.saveAll(autores);
        Long i = 1L;

        while (i <= autores.size()) {
            AutorLivroView view = autorRepository.findAutorLivroViewById(i);
            Assertions.assertNotNull(view);

            i++;
        }

        while (i <= autores.size() + 5) {
            AutorLivroView view = autorRepository.findAutorLivroViewById(i);
            Assertions.assertNull(view);

            i++;
        }
    }
}