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
import java.util.ArrayList;
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

    private static Stream<Arguments> providenciaAutoresInvalidosForValidacaoAutorVazioOuNull() {
        List<String> nomes = Arrays.asList(null, "", "       ");
        List<String> emails = Arrays.asList(null, "", "       ");
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

    private static Stream<Arguments> providenciaAutorParaValidacaoEmail() {
        List<String> emailsInvalidos = List.of("emailInvalido", "email.invalido.com", "EmAIlInvaLiDo5");
        List<String> emailsValidos = List.of("email.valido@email.com", "EmAiL.VALIDO2@email.com");

        Stream.Builder<Arguments> stream = Stream.builder();
        AutorFormBuilder builder = new AutorFormBuilder();

        for (String invalido : emailsInvalidos) {
            stream.add(Arguments.of(builder
                    .nome("Nome Autor")
                    .email(invalido)
                    .descricao("Descrição Autor").build(), false
            ));
        }

        for (String valido : emailsValidos) {
            stream.add(Arguments.of(builder
                    .nome("Nome Autor")
                    .email(valido)
                    .descricao("Descrição Autor").build(), true
            ));
        }

        return stream.build();
    }

    private static Stream<Arguments> providenciaEmailsDuplicados() {
        String nome = "Nome Qualquer";
        String descricao = "Descrição qualquer...";

        List<String> emails = List.of("um.email@email.com",
                "OuTRo.Email@email.com",
                "Joaquina.Ellionel@email.com");

        List<Autor> autores = new ArrayList<Autor>();
        AutorBuilder autorBuilder = new AutorBuilder();
        AutorFormBuilder autorFormBuilder = new AutorFormBuilder().nome(nome).descricao(descricao);

        for (String email : emails) {
            autores.add(autorBuilder.nome(nome).descricao(descricao).email(email).build());
        }

        List<String> validos = List.of("email.inexistente@email.com",
                "outro.email@inexistente");

        List<String> invalidos = List.of("um.email@email.com",
                "outro.email@email.com");

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String valido : validos) {
            AutorForm form = autorFormBuilder.email(valido).build();

            stream.add(Arguments.of(
                    autores, form, true
            ));
        }

        for (String invalido : invalidos) {
            AutorForm form = autorFormBuilder.email(invalido).build();

            stream.add(Arguments.of(
                    autores, form, false
            ));
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
    @MethodSource("providenciaAutoresInvalidosForValidacaoAutorVazioOuNull")
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

    @ParameterizedTest
    @MethodSource("providenciaAutorParaValidacaoEmail")
    public void validacaoAutorEmail(AutorForm autor, boolean valido) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertEquals(valido, errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providenciaEmailsDuplicados")
    public void validacaoEmailJaExistente(List<Autor> autores, AutorForm form, boolean valido) {
        autorRepository.saveAll(autores);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(form);

        Assertions.assertEquals(valido, errors.isEmpty());
    }
}