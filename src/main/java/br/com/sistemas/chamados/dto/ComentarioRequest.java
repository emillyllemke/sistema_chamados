package br.com.sistemas.chamados.dto;
public class ComentarioRequest {
    private String texto;
    private String autor;
    // Adicione os getters e setters
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
}