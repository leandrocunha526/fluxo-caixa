package org.api.fluxocaixa.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

@Data
@NoArgsConstructor
public class SaldoDiarioDTO {
    private LocalDate data;
    private BigDecimal totalCredito;
    private BigDecimal totalDebito;
    private BigDecimal saldoFinal;

    public SaldoDiarioDTO(LocalDate data,
                          BigDecimal totalCredito,
                          BigDecimal totalDebito,
                          BigDecimal saldoFinal) {
        this.data = data;
        this.totalCredito = totalCredito;
        this.totalDebito = totalDebito;
        this.saldoFinal = saldoFinal;
    }
}
