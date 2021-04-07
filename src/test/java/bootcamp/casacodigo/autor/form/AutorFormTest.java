package bootcamp.casacodigo.autor.form;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@DataJpaTest
class AutorFormTest {
    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private static Stream<Arguments> provideAutoresInvalidosForValidacaoAutorVazioOuNull() {
        List<String> nomes = Arrays.asList(null, "", "       ");
        List<String> emails = Arrays.asList(null, "", "       ", "emailinvalido");
        List<String> descricoes = Arrays.asList(null, "", "      ");

        Stream.Builder<Arguments> builder = Stream.builder();

        for (String nome : nomes) {
            for (String email : emails) {
                for (String descricao : descricoes) {
                    builder.add(Arguments.of(new AutorFormBuilder()
                            .nome(nome)
                            .email(email)
                            .descricao(descricao)
                            .build()));
                }
            }
        }

        return builder.build();
    }

    private static Stream<Arguments> providenciaAutorValidoParaValidacao() {
        List<String> nomes = Arrays.asList("João Joaquim", "Aline Joanete", "Ellionel Leão");
        List<String> emails = Arrays.asList("joao@joaquim.com", "aline@joanete.com", "ellionel.leao@email.com");
        List<String> descricoes = Arrays.asList("Este é o Dr. João.", "Esta é a Dra. Aline.", "Este é o Prof. Ellionel.");

        AutorFormBuilder builder = new AutorFormBuilder();
        Stream.Builder<Arguments> streamBuilder = Stream.builder();

        for (String nome : nomes) {
            for (String email : emails) {
                for (String descricao : descricoes) {
                    streamBuilder.add(Arguments.of(
                            builder.nome(nome).email(email).descricao(descricao).build()
                    ));
                }
            }
        }

        return streamBuilder.build();
    }

    private static Stream<Arguments> providenciaAutorParaValidacaoDuplicada() {
        List<AutorForm> autores = List.of(
                new AutorForm("João Joaquim", "joao@joaquim.com", "Este é o Dr. João."),
                new AutorForm("Aline Joanete", "aline@joanete.com", "Esta é a Dra. Aline."),
                new AutorForm("Ellionel Leão", "ellionel.leao@email.com", "Este é o Prof. Ellionel.")
        );

        Stream.Builder<Arguments> stream = Stream.builder();

        for (AutorForm form : autores) {
            for (AutorForm autor : autores) {
                stream.add(Arguments.of(
                        form, autor.converte(), form != autor
                ));
            }
        }

        return stream.build();
    }

    @BeforeEach
    public void setup() {
        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @MethodSource("provideAutoresInvalidosForValidacaoAutorVazioOuNull")
    public void validacaoAutorVazioOuNull(AutorForm autor) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorValidoParaValidacao")
    public void validacaoAutorValido(AutorForm autor) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorParaValidacaoDuplicada")
    public void validacaoAutorJaExistente(AutorForm autor, Autor existente, boolean valido) {
        autorRepository.save(existente);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);

        Assertions.assertEquals(valido, errors.isEmpty());
    }
}