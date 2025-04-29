package org.api.fluxocaixa.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Entity
@Data
@Getter
@Setter
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull(message = "A data do lançamento é obrigatória")
    private LocalDate data;

    @NotNull(message = "O tipo de lançamento (CREDITO/DEBITO) é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor (R$) deve ser maior que zero")
    private BigDecimal valor;

    @Size(max = 200, message = "A descrição pode ter no máximo 200 caracteres")
    private String descricao;
}
