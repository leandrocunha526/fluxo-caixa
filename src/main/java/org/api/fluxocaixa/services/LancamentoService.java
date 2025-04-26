package org.api.fluxocaixa.services;

import lombok.RequiredArgsConstructor;
import org.api.fluxocaixa.dtos.SaldoDiarioDTO;
import org.api.fluxocaixa.models.Lancamento;
import org.api.fluxocaixa.repositories.LancamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public List<SaldoDiarioDTO> calcularSaldoPorData() {
        return lancamentoRepository.calcularSaldoDiario();
    }
    
    public List<Lancamento> listarTodos() {
        return lancamentoRepository.findAll();
    }
    
    public Lancamento buscarPorId(Long id) {
        return lancamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lançamento não encontrado com ID: " + id));
    } 
    
    public void deletar(Long id) {
		Lancamento lancamento = lancamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lançamento não encontrado com ID: " + id));
        lancamentoRepository.delete(lancamento);
	}
	
	public Lancamento atualizar(Long id, Lancamento novoLancamento) {
		Lancamento lancamento = lancamentoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento não encontrado com ID: " + id));

		lancamento.setDescricao(novoLancamento.getDescricao());
		lancamento.setValor(novoLancamento.getValor());
		lancamento.setTipo(novoLancamento.getTipo());
		lancamento.setData(novoLancamento.getData());

		return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> buscarPorData(LocalDate data) {
        return lancamentoRepository.findByData(data);
    }
}
