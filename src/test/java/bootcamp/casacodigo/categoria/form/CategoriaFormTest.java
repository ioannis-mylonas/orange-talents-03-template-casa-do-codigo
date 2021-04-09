package bootcamp.casacodigo.categoria.form;

import bootcamp.casacodigo.autor.form.AutorForm;
import bootcamp.casacodigo.autor.form.AutorFormBuilder;
import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

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

    public static List<Categoria> geraCategorias() {
        CategoriaBuilder builder = new CategoriaBuilder();
        return List.of(
                builder.nome("Ação").build(),
                builder.nome("Aventura").build(),
                builder.nome("Ficção Científica").build(),
                builder.nome("Romance").build(),
                builder.nome("Mistério").build()
        );
    }

    public static Stream<String> providenciaCategoriaFormsComNomesInvalidos() {
        return Stream.of(null, "", "        ");
    }

    public static Stream<String> providenciaCategoriaFormsComNomesValidos() {
        return Stream.of("Ação", "Ficção Científica", "Romance");
    }

    public static Stream<Arguments> providenciaNomeParaTesteNomeExistente() {
        List<Categoria> categorias = geraCategorias();
        return Stream.of(
                Arguments.of(categorias, "Ação", false),
                Arguments.of(categorias, "mistério", false),
                Arguments.of(categorias, "Comédia", true)
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
    @MethodSource("providenciaCategoriaFormsComNomesInvalidos")
    public void testaValidacaoNomeInvalido(String nome) {
        CategoriaForm form = new CategoriaFormBuilder().nome(nome).build();
        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Nome %s deveria ser inválido!", nome
        ));
    }

    @ParameterizedTest
    @MethodSource("providenciaCategoriaFormsComNomesValidos")
    public void testaValidacaoNomeValido(String nome) {
        CategoriaForm form = new CategoriaFormBuilder().nome(nome).build();
        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);
        Assertions.assertTrue(errors.isEmpty(), String.format(
                "Nome %s deveria ser válido!", nome
        ));
    }

    @ParameterizedTest
    @MethodSource("providenciaNomeParaTesteNomeExistente")
    public void testaValidacaoNomeExistente(List<Categoria> categorias,
                                            String nome, boolean valido) {
        categoriaRepository.saveAll(categorias);
        CategoriaFormBuilder builder = new CategoriaFormBuilder();
        CategoriaForm form = builder.nome(nome).build();

        Set<ConstraintViolation<CategoriaForm>> errors = validator.validate(form);

        Assertions.assertEquals(valido, errors.isEmpty(), String.format(
                "Teste de busca por nome existente %s falhou!", nome
        ));
    }
}