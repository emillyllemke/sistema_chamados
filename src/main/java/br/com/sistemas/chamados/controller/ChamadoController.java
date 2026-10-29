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
    public ResponseEntity<List<ChamadoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
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