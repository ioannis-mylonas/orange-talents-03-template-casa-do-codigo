package bootcamp.casacodigo.livro.form;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class LivroFormTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    private Map<String, Map<Class<?>, List<Object>>> valoresValidos = Map.of(
            "setTitulo", Map.of(String.class, List.of(
                    "O Vôo das Galinhas", "A Vida de Clarisbela", "Balões que Voam")),
            "setResumo", Map.of(String.class, List.of(
                    "Um livro emocionante.", "Um épico clássico.", "Incrível!")),
            "setSumario", Map.of(String.class, List.of(
                    "Meu sumário...", "Um grande sumário...", "Outro sumário válido.")),
            "setIsbn", Map.of(String.class, List.of(
                    "9781234567897", "0-2866-7706-7", "978-1-8723-9692-7", "0-5120-0565-6")),
            "setPreco", Map.of(BigDecimal.class, List.of(
                    BigDecimal.valueOf(27.31), BigDecimal.valueOf(35.42),
                    BigDecimal.valueOf(52.31), BigDecimal.valueOf(25.64),
                    BigDecimal.valueOf(55.83))),
            "setPaginas", Map.of(Integer.class, List.of(
                    172, 521, 312, 975, 576, 478, 124)),
            "setPublicacao", Map.of(LocalDate.class, List.of(
                    LocalDate.ofInstant(Instant.now().plusSeconds(526000), ZoneId.of("UTC")),
                    LocalDate.ofInstant(Instant.now().plusSeconds(756000), ZoneId.of("UTC")),
                    LocalDate.ofInstant(Instant.now().plusSeconds(842500), ZoneId.of("UTC")))),
            "setCategoriaId", Map.of(Long.class, List.of(
                    1L, 2L, 3L)),
            "setAutorId", Map.of(Long.class, List.of(
                    1L, 2L))
    );

    private Object pickRandomValue(List<Object> values) {
        Random random = new Random();
        int index = random.nextInt(values.size());
        return values.get(index);
    }

    private LivroFormBuilder generateRandomValidFormBuilder() throws Exception {
        LivroFormBuilder builder = new LivroFormBuilder();

        for (String key : valoresValidos.keySet()) {
            Map<Class<?>, List<Object>> values = valoresValidos.get(key);
            for (Class<?> castTo : values.keySet()) {
                Method method = builder.getClass().getMethod(key, castTo);
                method.setAccessible(true);

                List<Object> possible = values.get(castTo);
                Object value = pickRandomValue(possible);
                method.invoke(builder, castTo.cast(value));
            }
        }

        return builder;
    }

    private Stream<Arguments> provideLivroFormBuilderParaTesteCampoObrigatorioVazio() throws Exception {
        LivroFormBuilder builder = generateRandomValidFormBuilder();
        Stream.Builder<Arguments> stream = Stream.builder();

        List<String> stringsVazias = Arrays.asList("", "        ");

        for (String methodName : valoresValidos.keySet()) {
            for (Class<?> castTo : valoresValidos.get(methodName).keySet()) {
                Method method = builder.getClass().getMethod(methodName, castTo);
                method.setAccessible(true);

                method.invoke(builder, castTo.cast(null));
                stream.add(Arguments.of(
                        builder.build(), String.format(
                                "Campo de tipo %s com valor null deveria ser inválido!",
                                castTo.getName()
                        )
                ));

                if (castTo == String.class) {
                    for (String value : stringsVazias) {
                        method.invoke(builder, castTo.cast(value));
                        stream.add(Arguments.of(
                                builder.build(), String.format(
                                        "Campo de tipo %s com valor %s deveria ser inválido!",
                                        castTo.getName(), castTo.cast(value)
                                )
                        ));
                    }
                }

                Object value = pickRandomValue(valoresValidos.get(methodName).get(castTo));
                method.invoke(builder, castTo.cast(value));
            }
        }

        return stream.build();
    }

    @BeforeAll
    @Transactional
    public void setupClass() {
        List<Categoria> categorias = List.of(
                new Categoria("Ação"),
                new Categoria("Mistério"),
                new Categoria("Aventura")
        );

        List<Autor> autores = List.of(
                new Autor("Josefino da Silva", "josefino.silva@email.com", "Dr. Josefino"),
                new Autor("Alessandra dos Santos", "alessandra.santos@email.com", "Dra. Alessandra")
        );

        categoriaRepository.saveAll(categorias);
        autorRepository.saveAll(autores);
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
    @MethodSource("provideLivroFormBuilderParaTesteCampoObrigatorioVazio")
    public void testaCampoVazio(LivroForm form, String mensagem) {
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty(), mensagem);
    }
}
