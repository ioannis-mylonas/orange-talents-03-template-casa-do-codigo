package bootcamp.casacodigo.cliente.form;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
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

    private ClienteFormBuilder getRandomValidFormBuilder() throws Exception {
        ClienteFormBuilder builder = new ClienteFormBuilder();

        for (String key : valoresStringValidos.keySet()) {
            Method method = builder.getClass().getMethod(key, String.class);
            method.setAccessible(true);
            Random random = new Random();

            List<String> possibilidades = valoresStringValidos.get(key);

            int index = random.nextInt(possibilidades.size());
            method.invoke(builder, possibilidades.get(index));
        }

        builder.setPaisId(getPaisSemEstados().getId());

        return builder;
    }

    private static Stream<Arguments> providenciaClienteFormParaValidacaoCamposVazios() throws Exception {
        List<String> valoresInvalidos = Arrays.asList(null, "", "     ");
        Stream.Builder<Arguments> stream = Stream.builder();

        for (String key : valoresStringValidos.keySet()) {
            Method method = ClienteFormBuilder.class.getMethod(key, String.class);
            method.setAccessible(true);

            for (String texto : valoresInvalidos) {
                stream.add(Arguments.of(
                        method, texto, false, String.format(
                                "Método %s colocado com valor %s deveria ser inválido!",
                                key, texto
                        )
                ));
            }

            for (String texto : valoresStringValidos.get(key)) {
                stream.add(Arguments.of(
                        method, texto, true, String.format(
                                "Método %s colocado com valor %s deveria ser válido!",
                                key, texto
                        )
                ));
            }
        }

        return stream.build();
    }

    private static Stream<Arguments> providenciaClienteFormParaValidacaoEmail() throws Exception {
        List<String> emailsInvalidos = Arrays.asList(
                "emailinvalido", "InVaLiDo", "email.invalido", "@", "algo@"
        );

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String email : emailsInvalidos) {
            stream.add(Arguments.of(email, false,
                    String.format("Email de valor %s deveria ser inválido!", email)
            ));
        }

        for (String email : valoresStringValidos.get("setEmail")) {
            stream.add(Arguments.of(email, true,
                    String.format("Email de valor %s deveria ser válido!", email)
            ));
        }

        return stream.build();
    }

    private Pais getPaisSemEstados() {
        List<Pais> paises = paisRepository.findAll();
        List<Estado> estados = estadoRepository.findAll();

        List<Pais> paisSemEstados = paises.stream().filter(i -> {
            for (Estado estado : estados) {
                if (estado.getPais().equals(i)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        Random random = new Random();
        int index = random.nextInt(paisSemEstados.size());
        return paisSemEstados.get(index);
    }

    private Pais getPaisComEstados() {
        List<Pais> paises = paisRepository.findAll();
        List<Estado> estados = estadoRepository.findAll();

        List<Pais> paisSemEstados = paises.stream().filter(i -> {
            for (Estado estado : estados) {
                if (estado.getPais().equals(i)) return true;
            }
            return false;
        }).collect(Collectors.toList());

        Random random = new Random();
        int index = random.nextInt(paisSemEstados.size());
        return paisSemEstados.get(index);
    }

    private Long getPaisInexistente() {
        Long i = 1L;
        while (true) {
            Optional<Pais> pais = paisRepository.findById(i);
            if (pais.isEmpty()) return i;

            i++;
        }
    }

    private Long getEstadoInexistente(Pais pais) {
        List<Estado> estados = estadoRepository.findByPais(pais);

        Long i = 1L;
        while (true) {
            Long id = i;
            boolean exists = estados.stream().anyMatch(estado -> {
                return estado.getId().equals(id);
            });

            if (!exists) return id;
            i++;
        }
    }

    private Long getEstadoExistente(Pais pais) {
        List<Estado> estados = estadoRepository.findByPais(pais);

        Long i = 1L;
        while (true) {
            Long id = i;
            boolean exists = estados.stream().anyMatch(estado -> {
                return estado.getId().equals(id);
            });

            if (exists) return id;
            i++;
        }
    }

    @BeforeEach
    public void setup() {
        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();

        Pais pais1L = paisRepository.save(new Pais("Pais 1L"));
        Pais pais2L = paisRepository.save(new Pais("Pais 2L"));
        Pais pais3L = paisRepository.save(new Pais("Pais 3L"));

        Estado estado1L = estadoRepository.save(new Estado("Estado 1L", pais2L));
        Estado estado2L = estadoRepository.save(new Estado("Estado 2L", pais2L));
    }

    @ParameterizedTest
    @MethodSource("providenciaClienteFormParaValidacaoCamposVazios")
    public void testaValidacaoCamposObrigatorios(Method method, String value, boolean valido, String mensagem) throws Exception {
        ClienteFormBuilder form = getRandomValidFormBuilder();
        method.invoke(form, value);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(form.build());
        Assertions.assertEquals(valido, errors.isEmpty(), mensagem);
    }

    @ParameterizedTest
    @MethodSource("providenciaClienteFormParaValidacaoEmail")
    public void testaValidacaoEmail(String email, boolean valido, String mensagem) throws Exception {
        ClienteFormBuilder form = getRandomValidFormBuilder().setEmail(email);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(form.build());
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
        Pais pais = getPaisSemEstados();
        ClienteFormBuilder builder = getRandomValidFormBuilder().setPaisId(pais.getId());

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty(), String.format(
                "País %s não tem estados e deveria ser válido!", pais.getId()
        ));
    }

    @Test
    public void testaValidacaoPaisComEstadoVazioInvalido() throws Exception {
        Pais pais = getPaisComEstados();
        ClienteFormBuilder builder = getRandomValidFormBuilder().setPaisId(pais.getId());

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "País %s tem estados e deveria ser inválido!", pais.getId()
        ));
    }

    @Test
    public void testaValidacaoPaisComEstadoInvalido() throws Exception {
        Pais pais = getPaisComEstados();
        Long estado = getEstadoInexistente(pais);
        ClienteFormBuilder builder = getRandomValidFormBuilder()
                .setPaisId(pais.getId()).setEstadoId(estado);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País 2L não tem estado 10L e deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisComEstadoValido() throws Exception {
        Pais pais = getPaisComEstados();
        Long estado = getEstadoExistente(pais);

        ClienteFormBuilder builder = getRandomValidFormBuilder()
                .setPaisId(pais.getId()).setEstadoId(estado);

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