package bootcamp.casacodigo.localizacao.form;

import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@DataJpaTest
class PaisFormTest {
    private List<Pais> paises = List.of(
            new Pais("Pais A"),
            new Pais("Pais B"),
            new Pais("Pais C"),
            new Pais("Pais D")
    );

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private ApplicationContext applicationContext;
    private SpringConstraintValidatorFactory constraintValidatorFactory;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    public void setup() {
        paisRepository.saveAll(paises);

        constraintValidatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(constraintValidatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaValidacaoDeNomeVazio(String nome) {
        PaisForm form = new PaisForm();
        form.setNome(nome);

        Set<ConstraintViolation<PaisForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "País de nome %s deveria ser considerado vazio ou null e inválido!", nome));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pais D", "pais a", "    Pais C   ", "    pais b    "})
    public void testaValidacaoNomeDuplicado(String nome) {
        PaisForm form = new PaisForm();
        form.setNome(nome);

        Set<ConstraintViolation<PaisForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "País de nome %s deveria ser duplicado!", nome));
    }

    @ParameterizedTest
    @CsvSource({"Pais H", "pais e", "Pais G", "Pais F"})
    public void testaValidacaoNomeValido(String nome) {
        PaisForm form = new PaisForm();
        form.setNome(nome);

        Set<ConstraintViolation<PaisForm>> errors = validator.validate(form);
        Assertions.assertTrue(errors.isEmpty(), String.format(
                "País de nome %s deveria ser válido!", nome));
    }
}