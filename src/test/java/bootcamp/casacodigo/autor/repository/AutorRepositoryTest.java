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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
class AutorRepositoryTest {
    @Autowired
    private AutorRepository autorRepository;

    private static List<Autor> geraAutoresValidos() {
        AutorBuilder builder = new AutorBuilder();

        return List.of(
                builder
                        .setNome("Mariana Soares")
                        .setEmail("mariana.soares@email.com")
                        .setDescricao("Uma Descricao").build(),
                builder
                        .setNome("João Joaquim")
                        .setEmail("joao.joaquim@email.com")
                        .setDescricao("Dr. João Joaquim").build(),
                builder
                        .setNome("Ellionel dos Santos")
                        .setEmail("ellionel.santos@email.com")
                        .setDescricao("Prof. Ellionel").build()
        );
    }

    private static Stream<Arguments> provideAutoresForConsultaEmail() {
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

    private static Stream<List<Autor>> providenciaAutoresValidosParaTeste() {
        List<Autor> autores = geraAutoresValidos();
        return Stream.of(autores);
    }

    @ParameterizedTest
    @MethodSource("provideAutoresForConsultaEmail")
    @Transactional
    private void testaBuscaEmailExistente(List<Autor> autores,
                                         List<String> emailsInexistentes,
                                         List<String> emailsExistentes) {

        autorRepository.saveAll(autores);

        for (String email : emailsInexistentes) {
            Optional<Autor> autor = autorRepository.findByEmailIgnoreCase(email);
            Assertions.assertFalse(autor.isPresent(), String.format(
                    "Email %s deveria existir mas não foi encontrado!", email
            ));
        }

        for (String email : emailsExistentes) {
            Optional<Autor> autor = autorRepository.findByEmailIgnoreCase(email);
            Assertions.assertTrue(autor.isPresent(), String.format(
                    "Email %s não deveria existir mas foi encontrado!", email
            ));
        }
    }

    @ParameterizedTest
    @MethodSource("providenciaAutoresValidosParaTeste")
    @Transactional
    private void testaBuscaAutorLivroView(List<Autor> autores) {
        autorRepository.saveAll(autores);
        Long i = 1L;

        while (i <= autores.size()) {
            AutorLivroView view = autorRepository.findAutorLivroViewById(i);
            Assertions.assertNotNull(view, String.format(
                    "Busca por id %s deveria ter encontrado mas retornou null", i
            ));

            i++;
        }

        while (i <= autores.size() + 5) {
            AutorLivroView view = autorRepository.findAutorLivroViewById(i);
            Assertions.assertNull(view, String.format(
                    "Busca por id %s não deveria ter encontrado nada mas retornou algo", i
            ));

            i++;
        }
    }
}