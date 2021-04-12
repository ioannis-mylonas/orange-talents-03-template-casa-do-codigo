package bootcamp.casacodigo.livro.controller;

import bootcamp.casacodigo.autor.form.AutorBuilder;
import bootcamp.casacodigo.autor.model.Autor;
import bootcamp.casacodigo.autor.repository.AutorRepository;
import bootcamp.casacodigo.autor.view.AutorLivroView;
import bootcamp.casacodigo.categoria.model.Categoria;
import bootcamp.casacodigo.categoria.repository.CategoriaRepository;
import bootcamp.casacodigo.livro.model.Livro;
import bootcamp.casacodigo.livro.repository.LivroRepository;
import bootcamp.casacodigo.livro.view.LivroDetalheView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class LivroControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LivroRepository mockLivroRepository;

    @Test
    public void testaGetLivroExistenteId() throws Exception {
        Optional<LivroDetalheView> resposta = Optional.of(
                new LivroDetalheView("Um Título", "Um Resumo", "Um Sumário",
                        "Um ISBN", BigDecimal.valueOf(150.00), 500,
                        new AutorLivroView("Um Autor", "Uma descrição de autor.")));

        Mockito.when(mockLivroRepository.findLivroDetalheById(1L)).thenReturn(resposta);

        mvc.perform(MockMvcRequestBuilders.get("/livros/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testaGetLivroInexistenteId() throws Exception {
        Optional<LivroDetalheView> resposta = Optional.empty();
        Mockito.when(mockLivroRepository.findLivroDetalheById(1000L)).thenReturn(resposta);

        mvc.perform(MockMvcRequestBuilders.get("/livros/1000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}