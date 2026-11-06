package br.com.sistemas.chamados.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataMudanca;

    @Enumerated(EnumType.STRING)
    private StatusChamado statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusChamado statusNovo;

    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    @PrePersist
    public void prePersist() {
        this.dataMudanca = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataMudanca() { return dataMudanca; }
    public void setDataMudanca(LocalDateTime dataMudanca) { this.dataMudanca = dataMudanca; }
    public StatusChamado getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(StatusChamado statusAnterior) { this.statusAnterior = statusAnterior; }
    public StatusChamado getStatusNovo() { return statusNovo; }
    public void setStatusNovo(StatusChamado statusNovo) { this.statusNovo = statusNovo; }
    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }
}