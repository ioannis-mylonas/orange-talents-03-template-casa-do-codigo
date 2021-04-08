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
import org.springframework.test.annotation.Rollback;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ClienteFormTest {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    public static Stream<Arguments> providenciaClienteFormComCamposObrigatoriosVazios() {
        List<String> invalidos = Arrays.asList(null, "", "     ");
        String validoPadrao = "Algum texto qualquer";
        String cpf = "256.769.470-63";
        String email = "um.email@valido.com";
        Long pais = 1L;

        ClienteFormBuilder builder = new ClienteFormBuilder()
                .setNome(validoPadrao)
                .setSobrenome(validoPadrao)
                .setEmail(email)
                .setDocumento(cpf)
                .setEndereco(validoPadrao)
                .setComplemento(validoPadrao)
                .setCidade(validoPadrao)
                .setPaisId(pais)
                .setTelefone(validoPadrao)
                .setCep(validoPadrao);

        Stream.Builder<Arguments> stream = Stream.builder();
        stream.add(Arguments.of(builder.build(), true));

        // Nome branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setNome(texto).build(), false
        ));
        builder.setNome(validoPadrao);

        // Sobrenome branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setSobrenome(texto).build(), false
        ));
        builder.setSobrenome(validoPadrao);

        // Email branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setEmail(texto).build(), false
        ));
        builder.setEmail(validoPadrao);

        // Endereço branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setEndereco(texto).build(), false
        ));
        builder.setEndereco(validoPadrao);

        // Complemento branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setComplemento(texto).build(), false
        ));
        builder.setComplemento(validoPadrao);

        // Cidade branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setCidade(texto).build(), false
        ));
        builder.setCidade(validoPadrao);

        // Telefone branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setTelefone(texto).build(), false
        ));
        builder.setTelefone(validoPadrao);

        // CEP branco ou null
        for (String texto : invalidos) stream.add(Arguments.of(
                builder.setCep(texto).build(), false
        ));
        builder.setCep(validoPadrao);

        return stream.build();
    }

    private static Stream<Arguments> providenciaClienteFormParaValidacaoEmail() {
        String validoPadrao = "Algum texto qualquer";
        String cpf = "256.769.470-63";
        Long pais = 1L;

        ClienteFormBuilder builder = new ClienteFormBuilder()
                .setNome(validoPadrao)
                .setSobrenome(validoPadrao)
                .setDocumento(cpf)
                .setEndereco(validoPadrao)
                .setComplemento(validoPadrao)
                .setCidade(validoPadrao)
                .setPaisId(pais)
                .setTelefone(validoPadrao)
                .setCep(validoPadrao);

        List<String> emailsInvalidos = Arrays.asList(
                "emailinvalido", "InVaLiDo", "email.invalido", "@", "algo@"
        );

        List<String> emailsValidos = Arrays.asList(
                "email@valido.com", "EmAiL@VALIDO.com", "email3@valido.com"
        );

        Stream.Builder<Arguments> stream = Stream.builder();

        for (String invalido : emailsInvalidos) {
            stream.add(Arguments.of(builder.setEmail(invalido).build(), false));
        }

        for (String valido : emailsValidos) {
            stream.add(Arguments.of(builder.setEmail(valido).build(), true));
        }

        return stream.build();
    }

    @BeforeAll
    public void classSetup() {
        paisRepository.save(new Pais("Um País Qualquer"));
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
    @MethodSource("providenciaClienteFormComCamposObrigatoriosVazios")
    public void testaValidacaoCamposObrigatorios(ClienteForm cliente, boolean valido) {
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(cliente);
        Assertions.assertEquals(valido, errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providenciaClienteFormParaValidacaoEmail")
    public void testaValidacaoEmail(ClienteForm form, boolean valido) {
        Set<ConstraintViolation<ClienteForm>> errors = validator.validate(form);
        Assertions.assertEquals(valido, errors.isEmpty());
    }
}