package bootcamp.casacodigo.cliente.form;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ClienteFormTest {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    private static Map<String, List<String>> valoresStringValidos = Map.of(
            "setNome", List.of("Um nome válido", "Joaquina", "Aline", "João", "Maria"),
            "setSobrenome", List.of("dos Santos", "Soares", "Marcela", "de Aquim"),
            "setEmail", List.of("valido@email.com", "Um.EmAiL@ValiDo.com"),
            "setEndereco", List.of("Rua dos Bobos, n.0", "Rua 309"),
            "setComplemento", List.of("Casa", "Apartamento 203", "Número 202"),
            "setCidade", List.of("Rio de Janeiro", "Pão de Queijo", "Internet"),
            "setTelefone", List.of("51 99784-4531", "74 8496-7345"),
            "setDocumento", List.of("884.258.190-91", "36.007.770/0001-03", "07495841060", "96652555000197"),
            "setCep", List.of("65051-330", "82515-095", "66025-013")
    );

    public static ClienteFormBuilder getRandomValidFormBuilder() throws Exception {
        ClienteFormBuilder builder = new ClienteFormBuilder().setPaisId(1L);

        for (String key : valoresStringValidos.keySet()) {
            Method method = builder.getClass().getMethod(key, String.class);
            method.setAccessible(true);
            Random random = new Random();

            List<String> possibilidades = valoresStringValidos.get(key);

            int index = random.nextInt(possibilidades.size());
            method.invoke(builder, possibilidades.get(index));
        }

        return builder;
    }

    public static Stream<Arguments> providenciaClienteFormParaValidacaoCamposVazios() throws Exception {
        List<String> valoresInvalidos = Arrays.asList(null, "", "     ");
        ClienteFormBuilder builder = getRandomValidFormBuilder();

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String key : valoresStringValidos.keySet()) {
            Method method = builder.getClass().getMethod(key, String.class);
            method.setAccessible(true);

            for (String texto : valoresInvalidos) {
                method.invoke(builder, texto);
                stream.add(Arguments.of(
                        builder.build(), false, String.format(
                                "Método %s colocado com valor %s deveria ser inválido!",
                                key, texto
                        )
                ));
            }

            for (String texto : valoresStringValidos.get(key)) {
                method.invoke(builder, texto);
                stream.add(Arguments.of(
                        builder.build(), true, String.format(
                                "Método %s colocado com valor %s deveria ser válido!",
                                key, texto
                        )
                ));
            }
        }

        return stream.build();
    }

    private static Stream<Arguments> providenciaClienteFormParaValidacaoEmail() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder();
        List<String> emailsInvalidos = Arrays.asList(
                "emailinvalido", "InVaLiDo", "email.invalido", "@", "algo@"
        );

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String email : emailsInvalidos) {
            stream.add(Arguments.of(builder.setEmail(email).build(), false,
                    String.format("Email de valor %s deveria ser inválido!", email)
            ));
        }

        for (String email : valoresStringValidos.get("setEmail")) {
            stream.add(Arguments.of(builder.setEmail(email).build(), true,
                    String.format("Email de valor %s deveria ser válido!", email)
            ));
        }

        return stream.build();
    }

    @BeforeAll
    @Transactional
    public void classSetup() {
        paisRepository.save(new Pais("Pais 1L"));
        Pais pais = paisRepository.save(new Pais("Pais 2L"));
        paisRepository.save(new Pais("Pais 3L"));

        estadoRepository.save(new Estado("Estado 1L", pais));
        estadoRepository.save(new Estado("Estado 2L", pais));
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
    @MethodSource("providenciaClienteFormParaValidacaoCamposVazios")
    public void testaValidacaoCamposObrigatorios(ClienteForm cliente, boolean valido, String mensagem) {
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(cliente);
        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaClienteFormParaValidacaoEmail")
    public void testaValidacaoEmail(ClienteForm form, boolean valido, String mensagem) {
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(form);
        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }

    @Test
    public void testaValidacaoPaisNull() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder().setPaisId(null);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País é obrigatório e valor null deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisSemEstados() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder().setPaisId(1L);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty(),
                "País 1L não tem estados e deveria ser válido!");
    }

    @Test
    public void testaValidacaoPaisComEstadoVazioInvalido() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder().setPaisId(2L);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País 2L tem estados e deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisComEstadoInvalido() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder()
                .setPaisId(2L).setEstadoId(10L);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País 2L não tem estado 10L e deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisComEstadoValido() throws Exception {
        ClienteFormBuilder builder = getRandomValidFormBuilder()
                .setPaisId(2L).setEstadoId(1L);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty(),
                "País 2L tem estado 1L e deveria ser válido!");
    }

    @Test
    public void testaValidacaoCpfCnpj() throws Exception {
        List<String> documentosInvalidos = List.of(
                "9665255500019", "19.219.506/000114", "49185727/0001-80",
                "744.614.40095", "744614.400-95", "74614400-95"
        );

        ClienteFormBuilder builder = getRandomValidFormBuilder();

        for (String documento : documentosInvalidos) {
            builder.setDocumento(documento);
            Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
            Assertions.assertFalse(errors.isEmpty(), String.format(
                    "Documento %s deveria ser inválido!", documento
            ));
        }

        for (String documento : valoresStringValidos.get("setDocumento")) {
            builder.setDocumento(documento);
            Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
            Assertions.assertTrue(errors.isEmpty(), String.format(
                    "Documento %s deveria ser válido!", documento
            ));
        }
    }
}