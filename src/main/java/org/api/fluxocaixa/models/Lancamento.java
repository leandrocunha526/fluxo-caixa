package org.api.fluxocaixa.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable= false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable= false)
    private TipoLancamento tipo;

    @Column(nullable= false)
    private BigDecimal valor;

    @Column(length = 200)
    private String descricao;
}
