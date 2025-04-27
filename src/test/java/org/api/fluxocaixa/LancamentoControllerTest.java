package org.api.fluxocaixa;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.api.fluxocaixa.controller.LancamentoController;
import org.api.fluxocaixa.model.Lancamento;
import org.api.fluxocaixa.model.TipoLancamento;
import org.api.fluxocaixa.service.LancamentoService;
import org.api.fluxocaixa.dto.SaldoDiarioDTO;
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
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("removal")
@RunWith(SpringRunner.class)
@WebMvcTest(LancamentoController.class)
public class LancamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LancamentoService lancamentoService;

    @Test
    public void testCriarLancamentos() throws Exception {
        // Criando os lançamentos
        Lancamento lancamento1 = new Lancamento();
        lancamento1.setId(1L);
        lancamento1.setDescricao("Lançamento de crédito");
        lancamento1.setValor(BigDecimal.valueOf(500.00));
        lancamento1.setData(LocalDate.of(2025, 4, 24));
        lancamento1.setTipo(TipoLancamento.CREDITO);

        Lancamento lancamento2 = new Lancamento();
        lancamento2.setId(2L);
        lancamento2.setDescricao("Lançamento de débito");
        lancamento2.setValor(BigDecimal.valueOf(200.00));
        lancamento2.setData(LocalDate.of(2025, 4, 24));
        lancamento2.setTipo(TipoLancamento.DEBITO);

        Lancamento lancamento3 = new Lancamento();
        lancamento3.setId(3L);
        lancamento3.setDescricao("Lançamento de crédito");
        lancamento3.setValor(BigDecimal.valueOf(1000.00));
        lancamento3.setData(LocalDate.of(2025, 4, 23));
        lancamento3.setTipo(TipoLancamento.CREDITO);

        Lancamento lancamento4 = new Lancamento();
        lancamento4.setId(4L);
        lancamento4.setDescricao("Lançamento de débito");
        lancamento4.setValor(BigDecimal.valueOf(250.00));
        lancamento4.setData(LocalDate.of(2025, 4, 23));
        lancamento4.setTipo(TipoLancamento.DEBITO);

        // Mockando o comportamento do serviço para salvar os lançamentos
        when(lancamentoService.salvar(any(Lancamento.class))).thenReturn(lancamento1, lancamento2, lancamento3, lancamento4);

        // Criando os lançamentos através de POST
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Enviar os lançamentos via POST
        mockMvc.perform(post("/api/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamento1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamento2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamento3)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamento4)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testConsultarSaldo() throws Exception {
        // Mockando os saldos diários esperados
        List<SaldoDiarioDTO> saldoDiario = Arrays.asList(
                new SaldoDiarioDTO(LocalDate.of(2025, 4, 23), BigDecimal.valueOf(1000.00), BigDecimal.valueOf(250.00), BigDecimal.valueOf(750.00)),
                new SaldoDiarioDTO(LocalDate.of(2025, 4, 24), BigDecimal.valueOf(700.00), BigDecimal.ZERO, BigDecimal.valueOf(700.00))
        );

        when(lancamentoService.calcularSaldoPorData()).thenReturn(saldoDiario);

        // Realizando a requisição GET para consultar o saldo diário
        mockMvc.perform(get("/api/saldo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data").value("2025-04-23"))
                .andExpect(jsonPath("$[0].totalCredito").value(1000))
                .andExpect(jsonPath("$[0].totalDebito").value(250))
                .andExpect(jsonPath("$[0].saldoFinal").value(750))
                .andExpect(jsonPath("$[1].data").value("2025-04-24"))
                .andExpect(jsonPath("$[1].totalCredito").value(700))
                .andExpect(jsonPath("$[1].totalDebito").value(0))
                .andExpect(jsonPath("$[1].saldoFinal").value(700));
    }
    @Test
    public void testEditarLancamento() throws Exception {
        // Mock lançamento existente atualizado
        Lancamento lancamentoAtualizado = new Lancamento();
        lancamentoAtualizado.setId(1L);
        lancamentoAtualizado.setDescricao("Lançamento de crédito atualizado");
        lancamentoAtualizado.setValor(BigDecimal.valueOf(600.00));
        lancamentoAtualizado.setData(LocalDate.of(2025, 4, 24));
        lancamentoAtualizado.setTipo(TipoLancamento.CREDITO);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(lancamentoService.atualizar(any(Long.class), any(Lancamento.class))).thenReturn(lancamentoAtualizado);

        mockMvc.perform(put("/api/lancamento/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(lancamentoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Lançamento de crédito atualizado"))
                .andExpect(jsonPath("$.valor").value(600.00));
    }

    @Test
    public void testPesquisarPorData() throws Exception {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);
        lancamento.setDescricao("Lançamento exemplo");
        lancamento.setValor(BigDecimal.valueOf(300.00));
        lancamento.setData(LocalDate.of(2025, 4, 24));
        lancamento.setTipo(TipoLancamento.CREDITO);

        List<Lancamento> lancamentos = List.of(lancamento);

        when(lancamentoService.buscarPorData(LocalDate.of(2025, 4, 24))).thenReturn(lancamentos);

        mockMvc.perform(get("/api/lancamento/data")
                        .param("data", "2025-04-24")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Lançamento exemplo"));
    }

    @Test
    public void testListarTodosLancamentos() throws Exception {
        Lancamento lancamento1 = new Lancamento();
        lancamento1.setId(1L);
        lancamento1.setDescricao("Primeiro lançamento");
        lancamento1.setValor(BigDecimal.valueOf(100.00));
        lancamento1.setData(LocalDate.of(2025, 4, 22));
        lancamento1.setTipo(TipoLancamento.CREDITO);

        Lancamento lancamento2 = new Lancamento();
        lancamento2.setId(2L);
        lancamento2.setDescricao("Segundo lançamento");
        lancamento2.setValor(BigDecimal.valueOf(200.00));
        lancamento2.setData(LocalDate.of(2025, 4, 23));
        lancamento2.setTipo(TipoLancamento.DEBITO);

        List<Lancamento> lancamentos = Arrays.asList(lancamento1, lancamento2);

        when(lancamentoService.listarTodos()).thenReturn(lancamentos);

        mockMvc.perform(get("/api/lancamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testBuscarLancamentoPorId() throws Exception {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);
        lancamento.setDescricao("Buscar lançamento por ID");
        lancamento.setValor(BigDecimal.valueOf(150.00));
        lancamento.setData(LocalDate.of(2025, 4, 24));
        lancamento.setTipo(TipoLancamento.CREDITO);

        when(lancamentoService.buscarPorId(1L)).thenReturn(lancamento);

        mockMvc.perform(get("/api/lancamento/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Buscar lançamento por ID"));
    }

    @Test
    public void testRemoverLancamento() throws Exception {
        // Simulando o sucesso na remoção do lançamento
        doNothing().when(lancamentoService).deletar(1L);  // Apenas simula a execução sem retorno

        // Realizando a requisição DELETE para remover o lançamento
        mockMvc.perform(delete("/api/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Espera-se um status 200 OK
                .andExpect(jsonPath("$").value("Excluído com sucesso!"));  // Espera a mensagem de sucesso
    }
}
