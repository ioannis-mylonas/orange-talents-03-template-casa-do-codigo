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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
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

    private static Stream<Arguments> provideAutoresForValidacaoAutorVazioOuNull() {
        return Stream.of(
                Arguments.of(
                        new AutorFormBuilder()
                                .nome(null)
                                .email(null)
                                .descricao(null)
                                .build(),
                        false),
                Arguments.of(
                        new AutorFormBuilder()
                                .nome(null)
                                .email("um.email@email.com")
                                .descricao("Uma descrição")
                                .build(),
                        false),
                Arguments.of(
                        new AutorFormBuilder()
                                .nome("Autor Inválido")
                                .email(null)
                                .descricao("A descrição do autor...")
                                .build(),
                        false),
                Arguments.of(
                        new AutorFormBuilder()
                                .nome("Autor Inválido")
                                .email("email.valido@email.com")
                                .descricao(null)
                                .build(),
                        false),
                Arguments.of("", "", "", false),
                Arguments.of("", "um.email@email.com", "Uma descrição", false),
                Arguments.of("Meu Nome", "meu@email.com", "", false),
                Arguments.of("Outro grande nome", "", "Minha descrição", false),
                Arguments.of("João Joaquim", "joao.joaquim@email.com", "Ele se chama João", true),
                Arguments.of("Joaquina de Assis Magalhães", "joaquina@lookout.com", "Ela se chama Joaquina", true)
        );
    }

    private static Stream<Arguments> providenciaAutorparaValidacaoDuplicada() {
        return Stream.of(
                Arguments.of(
                        new AutorFormBuilder()
                                .nome("João Joaquim de Melos")
                                .email("joao.joaquim@melos.com")
                                .descricao("Autor Sr. Joaquim é formado.").build(),
                        new AutorBuilder()
                                .nome("João Joaquim de Melos")
                                .email("joao.joaquim@melos.com")
                                .descricao("Autor Sr. Joaquim é formado.").build(),
                        false)
        );
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
    @MethodSource("provideAutoresForValidacaoAutorVazioOuNull")
    public void validacaoAutorVazioOuNull(String nome, String email, String descricao, boolean esperado) {
        AutorForm autor = new AutorForm(nome, email, descricao);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);

        Assertions.assertEquals(esperado, errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorparaValidacaoDuplicada")
    public void validacaoAutorJaExistente(AutorForm autor, Autor existente, boolean esperado) {
        autorRepository.save(existente);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);

        Assertions.assertEquals(esperado, errors.isEmpty());
    }
}