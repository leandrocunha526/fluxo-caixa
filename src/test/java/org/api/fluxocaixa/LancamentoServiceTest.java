package org.api.fluxocaixa;

import org.api.fluxocaixa.dto.SaldoDiarioDTO;
import org.api.fluxocaixa.model.Lancamento;
import org.api.fluxocaixa.model.TipoLancamento;
import org.api.fluxocaixa.repository.LancamentoRepository;
import org.api.fluxocaixa.service.LancamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class LancamentoServiceTest {

    @Mock
    private LancamentoRepository lancamentoRepository;

    @InjectMocks
    private LancamentoService lancamentoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void deveCalcularSaldoDiarioCorretamente() {
        // Arrange
        List<SaldoDiarioDTO> saldoDiarioDTOs = List.of(
                new SaldoDiarioDTO(
                        LocalDate.of(2025, 5, 1),
                        new BigDecimal("300.00"), // totalCredito
                        new BigDecimal("100.00"), // totalDebito
                        new BigDecimal("200.00")  // saldoFinal
                )
        );
        when(lancamentoRepository.calcularSaldoDiario()).thenReturn(saldoDiarioDTOs);

        // Act
        List<SaldoDiarioDTO> resultado = lancamentoService.calcularSaldoPorData();

        // Assert
        assertThat(resultado).isNotEmpty();
        SaldoDiarioDTO saldoDTO = resultado.getFirst();
        assertThat(saldoDTO.getData()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(saldoDTO.getSaldoFinal()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void deveRetornarSaldoVazioQuandoNaoHouverLancamentos() {
        // Arrange
        when(lancamentoRepository.calcularSaldoDiario()).thenReturn(List.of());

        // Act
        List<SaldoDiarioDTO> resultado = lancamentoService.calcularSaldoPorData();

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveListarApenasCreditosPorData() {
        // Arrange
        LocalDate data = LocalDate.of(2025, 5, 2);
        List<Lancamento> lancamentos = List.of(
                criarLancamento(data, TipoLancamento.CREDITO, new BigDecimal("100")),
                criarLancamento(data, TipoLancamento.DEBITO, new BigDecimal("50")),
                criarLancamento(data, TipoLancamento.CREDITO, new BigDecimal("300"))
        );
        when(lancamentoRepository.findByData(data)).thenReturn(lancamentos);

        // Act
        List<Lancamento> resultado = lancamentoService.buscarPorData(data);

        // Assert
        List<Lancamento> creditos = resultado.stream()
                .filter(l -> l.getTipo() == TipoLancamento.CREDITO)
                .toList();

        assertThat(creditos).hasSize(2);
        assertThat(creditos).allSatisfy(l -> assertThat(l.getTipo()).isEqualTo(TipoLancamento.CREDITO));
        assertThat(creditos).extracting(Lancamento::getValor)
                .containsExactlyInAnyOrder(new BigDecimal("100"), new BigDecimal("300"));
    }

    private Lancamento criarLancamento(LocalDate data, TipoLancamento tipo, BigDecimal valor) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(data);
        lancamento.setTipo(tipo);
        lancamento.setValor(valor);
        return lancamento;
    }
}
