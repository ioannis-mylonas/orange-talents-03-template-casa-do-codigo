package bootcamp.casacodigo.livro.form;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.*;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@DataJpaTest
public class LivroFormTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;

    private static Map<String, Map<Class<?>, List<Object>>> valoresValidos = Map.of(
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
                    LocalDate.ofInstant(Instant.now().plusSeconds(842500), ZoneId.of("UTC"))))
    );

    private static Object pickRandomValue(List<Object> values) {
        Random random = new Random();
        int index = random.nextInt(values.size());
        return values.get(index);
    }

    private Long getRandomExistingCategoriaId() {
        Random random = new Random();
        List<Categoria> categorias = categoriaRepository.findAll();
        int index = random.nextInt(categorias.size());
        return categorias.get(index).getId();
    }

    private Long getRandomExistingAutorId() {
        Random random = new Random();
        List<Autor> autores = autorRepository.findAll();
        int index = random.nextInt(autores.size());
        return autores.get(index).getId();
    }

    private Long getRandomInexistentCategoriaId() {
        List<Categoria> categorias = categoriaRepository.findAll();
        long i = 1L;
        while (true) {
            Long id = i;
            boolean exists = categorias.stream().anyMatch(categoria -> {
                return categoria.getId().equals(id);
            });

            if (!exists) return id;
            i++;
        }
    }

    private Long getRandomInexistentAutorId() {
        List<Autor> autores = autorRepository.findAll();
        long i = 1L;
        while (true) {
            Long id = i;
            boolean exists = autores.stream().anyMatch(autor -> {
                return autor.getId().equals(id);
            });

            if (!exists) return id;
            i++;
        }
    }

    private LivroFormBuilder generateRandomValidFormBuilder() throws Exception {
        LivroFormBuilder builder = new LivroFormBuilder()
                .setAutorId(getRandomExistingAutorId())
                .setCategoriaId(getRandomExistingCategoriaId());

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

    private static Stream<Arguments> provideLivroFormBuilderParaTesteCampoObrigatorioVazio() throws Exception {
        Stream.Builder<Arguments> stream = Stream.builder();

        List<String> stringsVazias = Arrays.asList("", "        ");

        for (String methodName : valoresValidos.keySet()) {
            for (Class<?> castTo : valoresValidos.get(methodName).keySet()) {
                Method method = LivroFormBuilder.class.getMethod(methodName, castTo);
                method.setAccessible(true);

                stream.add(Arguments.of(
                        method, castTo, null, String.format(
                                "Campo de tipo %s com valor null deveria ser inválido!",
                                castTo.getName()), false
                ));

                if (castTo == String.class) {
                    for (String value : stringsVazias) {
                        stream.add(Arguments.of(
                                method, castTo, value, String.format(
                                        "Campo de tipo %s com valor %s deveria ser inválido!",
                                        castTo.getName(), castTo.cast(value)), false
                        ));
                    }
                }

                Object value = pickRandomValue(valoresValidos.get(methodName).get(castTo));
                stream.add(Arguments.of(method, castTo, value, String.format(
                        "Campo de tipo %s com valor %s deveria ser válido!",
                        castTo.getName(), castTo.cast(value)), true
                ));
            }
        }

        return stream.build();
    }

    @BeforeEach
    public void setup() {
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

        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @MethodSource("provideLivroFormBuilderParaTesteCampoObrigatorioVazio")
    public void testaCampoVazio(Method method, Class<?> castTo,
                                Object value, String mensagem,
                                boolean esperado) throws Exception {

        LivroFormBuilder builder = generateRandomValidFormBuilder();
        method.invoke(builder, castTo.cast(value));

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertEquals(esperado, errors.isEmpty(), mensagem);
    }

    @Test
    public void testaCategoriaInexistente() throws Exception {
        LivroFormBuilder builder = generateRandomValidFormBuilder();
        long id = getRandomInexistentCategoriaId();
        builder.setCategoriaId(id);

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Categoria de id %s não existe, errors não deveria estar vazio!", id
        ));
    }

    @Test
    public void testaAutorInexistente() throws Exception {
        LivroFormBuilder builder = generateRandomValidFormBuilder();
        long id = getRandomInexistentAutorId();
        builder.setAutorId(id);

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Autor de id %s não existe, errors não deveria estar vazio!", id
        ));
    }
}
