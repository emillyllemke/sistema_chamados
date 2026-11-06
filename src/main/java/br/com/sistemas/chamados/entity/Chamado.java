package br.com.sistemas.chamados.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @PrePersist
    public void prePersist() {
        this.status = StatusChamado.ABERTO;
        this.dataAbertura = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();

    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL)
    private List<HistoricoStatus> historico = new ArrayList<>();

    public List<HistoricoStatus> getHistorico() { return historico; }
    public void setHistorico(List<HistoricoStatus> historico) { this.historico = historico; }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }
    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }
    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
    public StatusChamado getStatus() {
        return status;
    }
    public void setStatus(StatusChamado status) {
        this.status = status;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public Tecnico getTecnico() {
        return tecnico;
    }
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}