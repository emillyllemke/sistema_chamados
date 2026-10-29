package br.com.sistemas.chamados.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime data;
    private String autor;

    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    @PrePersist
    public void prePersist() {
        this.data = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }
}