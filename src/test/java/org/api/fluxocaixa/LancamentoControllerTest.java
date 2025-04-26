package org.api.fluxocaixa;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.api.fluxocaixa.controllers.LancamentoController;
import org.api.fluxocaixa.models.Lancamento;
import org.api.fluxocaixa.models.TipoLancamento;
import org.api.fluxocaixa.services.LancamentoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@WebMvcTest(LancamentoController.class)
public class LancamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LancamentoService lancamentoService;

    @Test
    public void testCreateLancamento() throws Exception {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L); // necessário para que o jsonPath("$.id") não falhe
        lancamento.setDescricao("Lançamento de exemplo");
        lancamento.setValor(BigDecimal.valueOf(100.00));
        lancamento.setData(LocalDate.now());
        lancamento.setTipo(TipoLancamento.CREDITO);

        when(lancamentoService.salvar(any(Lancamento.class))).thenReturn(lancamento);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/api/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(lancamento.getId()))
                .andExpect(jsonPath("$.descricao").value(lancamento.getDescricao()))
                .andExpect(jsonPath("$.valor").value(lancamento.getValor()))
                .andExpect(jsonPath("$.data").value(lancamento.getData().toString()))
                .andExpect(jsonPath("$.tipo").value("Crédito"));
    }
}
