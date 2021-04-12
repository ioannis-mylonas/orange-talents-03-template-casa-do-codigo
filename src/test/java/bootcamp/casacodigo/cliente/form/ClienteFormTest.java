package bootcamp.casacodigo.cliente.form;

import bootcamp.casacodigo.cliente.repository.ClienteRepository;
import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.*;

@DataJpaTest
class ClienteFormTest {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private List<Pais> paises = List.of();
    private List<Estado> estados = List.of();
    private ClienteFormBuilder builder;

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

    private int pickRandomIndex(List<?> values) {
        Random random = new Random();
        return random.nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    private ClienteFormBuilder getRandomValidFormBuilder() throws Exception {
        ClienteFormBuilder builder = new ClienteFormBuilder();

        for (String key : valoresStringValidos.keySet()) {
            Method method = builder.getClass().getMethod(key, String.class);
            method.setAccessible(true);

            List<String> possibilidades = valoresStringValidos.get(key);
            method.invoke(builder, pickRandomValue(possibilidades));
        }

        builder.setPaisId(getPaisSemEstados().getId());

        return builder;
    }

    private Pais getPaisSemEstados() {
        for (Pais pais : paises) {
            boolean temEstados = estados.stream()
                    .anyMatch(estado -> estado.getPais().getId().equals(pais.getId()));

            if (!temEstados) return pais;
        }
        throw new NoSuchElementException();
    }

    private Pais getPaisComEstados() {
        Estado estado = getEstadoExistente();
        return estado.getPais();
    }

    private Long getEstadoIdInexistente(Pais pais) {
        long i = 1L;
        while (true) {
            Long id = i;
            boolean exists = estados.stream().anyMatch(estado -> estado.getId().equals(id));

            if (!exists) return id;
            i++;
        }
    }

    private Estado getEstadoExistente() {
        return pickRandomValue(estados);
    }

    @BeforeEach
    public void setup() throws Exception {
        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();

        paises = paisRepository.saveAll(List.of(
                new Pais("Pais 1L"),
                new Pais("Pais 2L"),
                new Pais("Pais 3L")
        ));

        Pais pais = getPaisSemEstados();

        estados = estadoRepository.saveAll(List.of(
                new Estado("Estado 1L", pais),
                new Estado("Estado 2L", pais)
        ));

        builder = getRandomValidFormBuilder();
        Set<ConstraintViolation<ClienteFormBuilder>> errors = validator.validate(builder);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaNomeVazioInvalido(String nome) {
        builder.setNome(nome);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaSobrenomeVazioInvalido(String sobrenome) {
        builder.setNome(sobrenome);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaEnderecoInvalido(String endereco) {
        builder.setNome(endereco);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaComplementoVazioInvalido(String complemento) {
        builder.setNome(complemento);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaCidadeVazioInvalido(String cidade) {
        builder.setNome(cidade);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaTelefoneVazioInvalido(String telefone) {
        builder.setNome(telefone);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaCepVazioInvalido(String cep) {
        builder.setNome(cep);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"emailinvalido", "InVaLiDo", "email.invalido", "@", "algo@"})
    public void testaValidacaoEmailInvalido(String email) {
        builder.setEmail(email);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Email %s deveria ser inválido!", email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"valido@email.com", "Um.EmAiL@ValiDo.com"})
    public void testaValidacaoEmailDuplicado(String email) throws Exception {
        ClienteFormBuilder duplicado = getRandomValidFormBuilder()
                .setDocumento("882.068.510-89")
                .setEmail(email);

        clienteRepository.save(duplicado.build().converte(estadoRepository, paisRepository));

        builder.setEmail(email.toUpperCase());
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Email %s já existe e deveria ser inválido!", email));
    }

    @ParameterizedTest
    @NullSource
    public void testaValidacaoPaisNull(Long id) {
        builder.setPaisId(id);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País é obrigatório e valor null deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisSemEstados() {
        Pais pais = getPaisSemEstados();
        builder.setPaisId(pais.getId());

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty(), String.format(
                "País %s não tem estados e deveria ser válido!", pais.getId()
        ));
    }

    @Test
    public void testaValidacaoPaisComEstadoVazioInvalido() {
        Pais pais = getPaisComEstados();
        builder.setPaisId(pais.getId());

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "País %s tem estados e deveria ser inválido!", pais.getId()
        ));
    }

    @Test
    public void testaValidacaoPaisComEstadoInvalido() {
        Pais pais = getPaisComEstados();
        Long estado = getEstadoIdInexistente(pais);

        builder.setPaisId(pais.getId()).setEstadoId(estado);

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(),
                "País 2L não tem estado 10L e deveria ser inválido!");
    }

    @Test
    public void testaValidacaoPaisComEstadoValido() {
        Estado estado = getEstadoExistente();
        Pais pais = estado.getPais();

        builder.setPaisId(pais.getId()).setEstadoId(estado.getId());

        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty(),
                "País 2L tem estado 1L e deveria ser válido!");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"884.258.190-9", "36.007.7/0001-03",
            "0749", "1.2.3-4"})
    public void testaValidacaoCpfCnpjInvalido(String documento) {
        builder.setDocumento(documento);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Documento %s deveria ser inválido!", documento
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"884.258.190-91", "36.007.770/0001-03",
            "07495841060", "96652555000197"})
    public void testaValidacaoCpfCnpjDuplicado(String documento) throws Exception {
        ClienteFormBuilder duplicado = getRandomValidFormBuilder().setDocumento(documento);
        clienteRepository.save(duplicado.build().converte(estadoRepository, paisRepository));

        String documentoSemCaracteresEspeciais = documento
                .replaceAll("[.\\-/]", "");

        builder.setDocumento(documentoSemCaracteresEspeciais);
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }
}