package bootcamp.casacodigo.livro.form;

import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
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

@DataJpaTest
public class LivroFormTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private List<Autor> autores;
    private List<Categoria> categorias;

    private SpringConstraintValidatorFactory validatorFactory;
    private LocalValidatorFactoryBean validator;
    private LivroFormBuilder builder;

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

    private int pickRandomIndex(List<?> values) {
        Random random = new Random();
        return random.nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        int index = pickRandomIndex(values);
        return values.get(index);
    }

    private Long getRandomExistingCategoriaId() {
        int index = pickRandomIndex(categorias);
        return categorias.get(index).getId();
    }

    private Long getRandomExistingAutorId() {
        int index = pickRandomIndex(autores);
        return autores.get(index).getId();
    }

    private Long getRandomInexistentCategoriaId() {
        long i = 1L;
        while (true) {
            Long id = i;
            Optional<Categoria> categoria = categorias.stream()
                    .filter(it -> it.getId().equals(id)).findFirst();

            if (categoria.isEmpty()) return i;
            i++;
        }
    }

    private Long getRandomInexistentAutorId() {
        long i = 1L;
        while (true) {
            Long id = i;
            Optional<Autor> autor = autores.stream()
                    .filter(it -> it.getId().equals(id)).findFirst();

            if (autor.isEmpty()) return i;
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

    @BeforeEach
    public void setup() throws Exception {
        categorias = categoriaRepository.saveAll(List.of(
                new Categoria("Ação"),
                new Categoria("Mistério"),
                new Categoria("Aventura")
        ));

        autores = autorRepository.saveAll(List.of(
                new Autor("Josefino da Silva", "josefino.silva@email.com", "Dr. Josefino"),
                new Autor("Alessandra dos Santos", "alessandra.santos@email.com", "Dra. Alessandra")
        ));

        validatorFactory = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(validatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();

        builder = generateRandomValidFormBuilder();
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaTituloVazioInvalido(String titulo) {
        builder.setTitulo(titulo);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaResumoVazioInvalido(String resumo) {
        builder.setResumo(resumo);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaSumarioVazioInvalido(String sumario) {
        builder.setSumario(sumario);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaIsbnVazioInvalido(String isbn) {
        builder.setIsbn(isbn);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaPrecoVazioInvalido(BigDecimal preco) {
        builder.setPreco(preco);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaPaginasVazioInvalido(Integer paginas) {
        builder.setPaginas(paginas);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaPublicacaoVazioInvalido(LocalDate publicacao) {
        builder.setPublicacao(publicacao);
        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @Test
    public void testaPublicacaoPassadaInvalido() {
        Instant instant = Instant.now().minusSeconds(580000);
        LocalDate publicacao = LocalDate.ofInstant(instant, ZoneId.of("UTC"));
        builder.setPublicacao(publicacao);

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty());
    }

    @Test
    public void testaCategoriaInexistente() {
        long id = getRandomInexistentCategoriaId();
        builder.setCategoriaId(id);

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Categoria de id %s não existe, errors não deveria estar vazio!", id
        ));
    }

    @Test
    public void testaAutorInexistente() {
        long id = getRandomInexistentAutorId();
        builder.setAutorId(id);

        Set<ConstraintViolation<LivroForm>> errors = validator.validate(builder.build());
        Assertions.assertFalse(errors.isEmpty(), String.format(
                "Autor de id %s não existe, errors não deveria estar vazio!", id
        ));
    }
}
