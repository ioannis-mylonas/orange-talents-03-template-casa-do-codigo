package bootcamp.casacodigo.localizacao.repository;

import bootcamp.casacodigo.localizacao.model.Estado;
import bootcamp.casacodigo.localizacao.model.Pais;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Random;

@DataJpaTest
class EstadoRepositoryTest {
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    private List<Pais> paises;

    private <T> T pickRandom(List<T> opcoes) {
        Random random = new Random();
        int index = random.nextInt(opcoes.size());
        return opcoes.get(index);
    }

    @BeforeEach
    public void setup() {
        paises = paisRepository.saveAll(List.of(
                new Pais("Pais A"),
                new Pais("Pais B"),
                new Pais("Pais C")
        ));
    }

    @Test
    public void testaEstadoEncontradoPorPais() {
        Pais pais = pickRandom(paises);
        Estado estado = estadoRepository.save(new Estado("Estado A", pais));

        List<Estado> estados = estadoRepository.findByPais(pais);
        Estado match = estados.get(0);

        Assertions.assertEquals(estado.getId(), match.getId(), String.format(
                "Estado encontrado de id %s diferente do esperado %s!",
                match.getId(), estado.getId()
        ));
    }

    @Test
    public void testaEstadoNaoEncontradoPaisSemEstados() {
        Pais pais = pickRandom(paises);
        List<Estado> estados = estadoRepository.findByPais(pais);

        Assertions.assertTrue(estados.isEmpty(), String.format(
                "Estados encontrados em país vazio de ID %s!", pais.getId()
        ));
    }
}