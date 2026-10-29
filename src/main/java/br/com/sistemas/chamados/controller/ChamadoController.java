package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.ChamadoRequest;
import br.com.sistemas.chamados.dto.ChamadoResponse;
import br.com.sistemas.chamados.entity.Chamado;
import br.com.sistemas.chamados.entity.StatusChamado;
import br.com.sistemas.chamados.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import br.com.sistemas.chamados.entity.Prioridade;

import java.util.List;
import java.util.Map; // <-- Importação adicionada para resolver o erro do Map

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService service;

    @Autowired
    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponse> criar(@RequestBody ChamadoRequest chamadoRequest) {
        ChamadoResponse response = service.salvar(chamadoRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ChamadoResponse>> listarComFiltros(
            @RequestParam(required = false) StatusChamado status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 10, sort = "dataAbertura") Pageable pageable) {

        Page<ChamadoResponse> pagina = service.listarComFiltros(status, prioridade, clienteId, pageable);
        return ResponseEntity.ok(pagina);
    }

    @PatchMapping("/{id}/atribuir")
    public ResponseEntity<Chamado> atribuirTecnico(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        Long tecnicoId = payload.get("tecnicoId");
        Chamado chamado = service.atribuirTecnico(id, tecnicoId);
        return ResponseEntity.ok(chamado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Chamado> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        // Converte a string do JSON para o Enum StatusChamado
        StatusChamado novoStatus = StatusChamado.valueOf(payload.get("status").toUpperCase());

        Chamado chamado = service.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(chamado);
    }
}