package org.api.fluxocaixa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.api.fluxocaixa.dtos.SaldoDiarioDTO;
import org.api.fluxocaixa.models.Lancamento;
import org.api.fluxocaixa.services.LancamentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Lançamentos", description = "Operações relacionadas aos lançamentos financeiros")
public class LancamentoController {

    private final LancamentoService service;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    // Criar uma transação
    @Operation(summary = "Cadastrar os lançamentos")
    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody Lancamento lancamento) {
        try {
            Lancamento salvo = service.salvar(lancamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            // Erro de regra de negócio
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            // Erro genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar lançamento: " + e.getMessage());
        }
    }

    // Consulta de saldo diário
    @Operation(summary = "Mostra saldo diário")
    @GetMapping("/saldo")
    public ResponseEntity<List<SaldoDiarioDTO>> consultarSaldo() {
        try {
            List<SaldoDiarioDTO> saldoDiario = service.calcularSaldoPorData();
            return ResponseEntity.ok(saldoDiario);
        } catch (Exception e) {
            // Logando o erro para debug (Call to 'printStackTrace()' should probably be replaced with more robust logging)
            // e.printStackTrace();
            // Retornando um erro genérico para o cliente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    // Listar todos os lançamentos
    @Operation(summary = "Listar todos os lançamentos")
    @GetMapping("/lancamentos")
    public ResponseEntity<List<Lancamento>> listarTodos() {
        try {
            List<Lancamento> lancamentos = service.listarTodos();
            return ResponseEntity.ok(lancamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
     // Atualizar lançamento
     @Operation(summary = "Atualizar os lançamentos")
     @PutMapping("/lancamento/{id}")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @RequestBody Lancamento lancamento) {
        try {
            return ResponseEntity.ok(service.atualizar(id, lancamento));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar lançamento.");
        }
    }

    // Deletar lançamento
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar um lançamento")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.ok().body("Excluído com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar lançamento.");
        }
    }
    
    // Buscar um lançamento por ID
    @Operation(summary = "Buscar lançamento por id")
    @GetMapping("/lancamento/{id}")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar lançamento.");
        }
    }

    @GetMapping("/lancamento/data")
    @Operation(summary = "Listar todos os lançamentos por data")
    public ResponseEntity<?> buscarPorData(@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<Lancamento> lancamentos = service.buscarPorData(data);
            return ResponseEntity.ok(lancamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
