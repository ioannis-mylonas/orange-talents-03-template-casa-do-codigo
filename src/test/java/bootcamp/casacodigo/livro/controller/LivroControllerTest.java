package bootcamp.casacodigo.livro.controller;

import bootcamp.casacodigo.autor.view.AutorLivroView;
import bootcamp.casacodigo.livro.repository.LivroRepository;
import bootcamp.casacodigo.livro.view.LivroDetalheView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
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