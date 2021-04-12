package bootcamp.casacodigo.localizacao.form;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import bootcamp.casacodigo.localizacao.repository.EstadoRepository;
import bootcamp.casacodigo.localizacao.repository.PaisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import java.util.Random;
import java.util.Set;

@DataJpaTest
public class EstadoFormTest {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private ApplicationContext context;

    private SpringConstraintValidatorFactory constraintValidatorFactory;
    private LocalValidatorFactoryBean validator;

    private List<Pais> paises = List.of(
            new Pais("Pais A"),
            new Pais("Pais B"),
            new Pais("Pais C"));

    private Object getRandom(List<?> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private Long getPaisIdNaoExistente() {
        long i = 1L;

        while (true) {
            Optional<Pais> pais = paisRepository.findById(i);
            if (pais.isEmpty()) return i;
            i++;
        }
    }

    private Long getPaisIdExistente() {
        long i = 1L;

        while (true) {
            Optional<Pais> pais = paisRepository.findById(i);
            if (pais.isPresent()) return i;
            i++;
        }
    }

    @BeforeEach
    public void setup() {
        List<Pais> saved = paisRepository.saveAll(paises);
        Pais pais = (Pais) getRandom(saved);
        estadoRepository.save(new Estado("Estado A", pais));

        constraintValidatorFactory = new SpringConstraintValidatorFactory(
                context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(constraintValidatorFactory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();
    }

    @Test
    public void testaEstadoComPaisNaoExistente() {
        Long pais = getPaisIdNaoExistente();
        EstadoForm form = new EstadoForm("Estado B", pais);

        Set<ConstraintViolation<EstadoForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty());
    }

    @Test
    public void testaEstadoComPaisExistente() {
        Long pais = getPaisIdExistente();
        EstadoForm form = new EstadoForm("Estado B", pais);

        Set<ConstraintViolation<EstadoForm>> errors = validator.validate(form);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaEstadoComNomeVazio(String nome) {
        Long pais = getPaisIdExistente();
        EstadoForm form = new EstadoForm(nome, pais);

        Set<ConstraintViolation<EstadoForm>> errors = validator.validate(form);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pais B", "pais c", "PaIs D", "   pais d    "})
    public void testaEstadoComNomeValido(String nome) {
        Long pais = getPaisIdExistente();
        EstadoForm form = new EstadoForm(nome, pais);

        Set<ConstraintViolation<EstadoForm>> errors = validator.validate(form);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pais B", "pais c", "PaIs D", "   pais d    "})
    public void testaEstadoComNomeDuplicado(String nome) {
        Long pais = getPaisIdExistente();
        EstadoForm unico = new EstadoForm(nome, pais);
        EstadoForm duplicado = new EstadoForm(nome, pais);

        Set<ConstraintViolation<EstadoForm>> errors = validator.validate(unico);
        Assertions.assertTrue(errors.isEmpty());

        estadoRepository.save(unico.converte(paisRepository));

        errors = validator.validate(duplicado);
        Assertions.assertFalse(errors.isEmpty());
    }
}
