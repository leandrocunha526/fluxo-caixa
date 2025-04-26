package org.api.fluxocaixa.repositories;

import org.api.fluxocaixa.dtos.SaldoDiarioDTO;
import org.api.fluxocaixa.models.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query("SELECT new org.api.fluxocaixa.dtos.SaldoDiarioDTO(" +
            "l.data, " +
            "SUM(CASE WHEN l.tipo = 'CREDITO' THEN l.valor ELSE 0 END), " +
            "SUM(CASE WHEN l.tipo = 'DEBITO' THEN l.valor ELSE 0 END), " +
            "(SUM(CASE WHEN l.tipo = 'CREDITO' THEN l.valor ELSE 0 END) - " +
            "SUM(CASE WHEN l.tipo = 'DEBITO' THEN l.valor ELSE 0 END))) " +
            "FROM Lancamento l GROUP BY l.data ORDER BY l.data")
    List<SaldoDiarioDTO> calcularSaldoDiario();

    List<Lancamento> findByData(LocalDate data);
}
