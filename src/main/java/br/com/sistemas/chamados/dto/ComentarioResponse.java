package br.com.sistemas.chamados.dto;
import br.com.sistemas.chamados.entity.Comentario;
import java.time.LocalDateTime;

public class ComentarioResponse {
    private Long id;
    private String texto;
    private String autor;
    private LocalDateTime data;

    public ComentarioResponse(Comentario c) {
        this.id = c.getId();
        this.texto = c.getTexto();
        this.autor = c.getAutor();
        this.data = c.getData();
    }
    // Adicione getters
    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public String getAutor() { return autor; }
    public LocalDateTime getData() { return data; }
}