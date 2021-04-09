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
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@DataJpaTest
class AutorFormTest {
    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private static Map<String, List<String>> valoresValidos = Map.of(
            "setNome", List.of("João Joaquim", "Aline Joanete", "Ellionel Leão"),
            "setEmail", List.of("joao@joaquim.com", "aline@joanete.com", "ellionel.leao@email.com"),
            "setDescricao", List.of("Este é o Dr. João.", "Esta é a Dra. Aline.", "Este é o Prof. Ellionel.")
    );

    private static AutorFormBuilder generateRandomValidForm() throws Exception {
        AutorFormBuilder builder = new AutorFormBuilder();

        for (String key : valoresValidos.keySet()) {
            Random random = new Random();
            Method method = builder.getClass().getMethod(key, String.class);
            method.setAccessible(true);

            List<String> values = valoresValidos.get(key);
            String value = values.get(random.nextInt(values.size()));
            method.invoke(builder, value);
        }

        return builder;
    }

    private static Stream<Arguments> providenciaAutoresInvalidosForValidacaoAutorVazioOuNull() throws Exception {
        List<String> valoresInvalidos = Arrays.asList(null, "", "       ");

        Stream.Builder<Arguments> builder = Stream.builder();
        AutorFormBuilder form = generateRandomValidForm();

        for (String key : valoresValidos.keySet()) {
            Method method = form.getClass().getMethod(key, String.class);
            method.setAccessible(true);

            for (String value : valoresInvalidos) {
                method.invoke(form, value);
                builder.add(Arguments.of(form.build(), String.format(
                        "Valor %s no método %s deveria ser inválido!",
                        value, key
                )));
            }

            Random random = new Random();
            List<String> values = valoresValidos.get(key);
            String value = values.get(random.nextInt(values.size()));
            method.invoke(form, value);
        }

        return builder.build();
    }

    private static Stream<Arguments> providenciaAutorValidoParaValidacao() {
        AutorFormBuilder builder = new AutorFormBuilder();
        Stream.Builder<Arguments> streamBuilder = Stream.builder();

        for (String nome : valoresValidos.get("setNome")) {
            for (String email : valoresValidos.get("setEmail")) {
                for (String descricao : valoresValidos.get("setDescricao")) {
                    streamBuilder.add(Arguments.of(
                            builder.setNome(nome).setEmail(email).setDescricao(descricao).build(),
                            String.format(
                                    "Nome %s, email %s, descrição %s deveriam ser válidos!",
                                    nome, email, descricao
                            )
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
                        form, autor.converte(), form != autor,
                        form != autor ? "Objetos diferentes, deveria ser válido!" :
                                "Objetos iguais, deveria ser inválido!"
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

        for (String email : emailsInvalidos) {
            stream.add(Arguments.of(builder
                    .setNome("Nome Autor")
                    .setEmail(email)
                    .setDescricao("Descrição Autor").build(), false, String.format(
                            "Email %s deveria ser inválido!", email
                    )
            ));
        }

        for (String email : emailsValidos) {
            stream.add(Arguments.of(builder
                    .setNome("Nome Autor")
                    .setEmail(email)
                    .setDescricao("Descrição Autor").build(), true, String.format(
                            "Email %s deveria ser válido!", email
                    )
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
        AutorFormBuilder autorFormBuilder = new AutorFormBuilder().setNome(nome).setDescricao(descricao);

        for (String email : emails) {
            autores.add(autorBuilder.setNome(nome).setDescricao(descricao).setEmail(email).build());
        }

        List<String> validos = List.of("email.inexistente@email.com",
                "outro.email@inexistente");

        List<String> invalidos = List.of("um.email@email.com",
                "outro.email@email.com");

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String email : validos) {
            AutorForm form = autorFormBuilder.setEmail(email).build();

            stream.add(Arguments.of(
                    autores, form, true, String.format(
                            "Email %s é único e deveria ser válido!", email
                    )
            ));
        }

        for (String email : invalidos) {
            AutorForm form = autorFormBuilder.setEmail(email).build();

            stream.add(Arguments.of(
                    autores, form, false, String.format(
                            "Email %s é duplicado e deveria ser inválido!", email
                    )
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
    public void validacaoAutorVazioOuNull(AutorForm autor, String mensagem) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertFalse(errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorValidoParaValidacao")
    public void validacaoAutorValido(AutorForm autor, String mensagem) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertTrue(errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorParaValidacaoDuplicada")
    public void validacaoAutorJaExistente(AutorForm autor, Autor existente, boolean valido, String mensagem) {
        autorRepository.save(existente);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);

        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaAutorParaValidacaoEmail")
    public void validacaoAutorEmail(AutorForm autor, boolean valido, String mensagem) {
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(autor);
        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaEmailsDuplicados")
    public void validacaoEmailJaExistente(List<Autor> autores, AutorForm form, boolean valido, String mensagem) {
        autorRepository.saveAll(autores);
        Set<ConstraintViolation<AutorForm>> errors = validator.validate(form);

        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }
}