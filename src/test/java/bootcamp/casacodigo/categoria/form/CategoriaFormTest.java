package bootcamp.casacodigo.categoria.form;

import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaFormTest {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    private List<Categoria> categorias = List.of(
            new Categoria("Ação"),
            new Categoria("Aventura"),
            new Categoria("Ficção Científica"),
            new Categoria("Romance"),
            new Categoria("Mistério")
    );

    private static Stream<String> providenciaCategoriaFormsComNomesInvalidos() {
        return Stream.of(null, "", "        ");
    }

    @BeforeEach
    public void setup() {
        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();

        categoriaRepository.saveAll(categorias);
    }

    @ParameterizedTest
    @MethodSource("providenciaCategoriaFormsComNomesInvalidos")
    public void testaValidacaoNomeInvalido(String nome) {
        CategoriaForm form = new CategoriaFormBuilder().nome(nome).build();

        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Nome %s deveria ser inválido!", nome
        ));
    }

    @ParameterizedTest
    @CsvSource({"Documentário", "Alienígenas", "Sobrenatural"})
    public void testaValidacaoNomeValido(String nome) {
        CategoriaForm form = new CategoriaFormBuilder().nome(nome).build();

        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);
        Assertions.assertTrue(errors.isEmpty(), String.format(
                "Nome %s deveria ser válido!", nome
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ação", "    mistério    ", "romance"})
    public void testaValidacaoNomeExistente(String nome) {
        CategoriaForm form = new CategoriaFormBuilder().nome(nome).build();

        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Teste de busca por nome existente %s falhou!", nome
        ));
    }
}