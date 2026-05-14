package br.edu.faculdade.feedback.model.entity;

import java.time.LocalDateTime;


public class Feedback {

    private int           id;
    private int           usuarioId;   // chave estrangeira para Usuarios
    private int           produtoId;   // chave estrangeira para Produtos
    private int           nota;        // valor de 1 a 5
    private String        comentario;
    private LocalDateTime dataCriacao; // preenchida automaticamente pelo banco



    public Feedback() {}


    public Feedback(int id, int usuarioId, int produtoId,
                    int nota, String comentario, LocalDateTime dataCriacao) {
        this.id          = id;
        this.usuarioId   = usuarioId;
        this.produtoId   = produtoId;
        this.nota        = nota;
        this.comentario  = comentario;
        this.dataCriacao = dataCriacao;
    }


    public Feedback(int usuarioId, int produtoId, int nota, String comentario) {
        this.usuarioId  = usuarioId;
        this.produtoId  = produtoId;
        this.nota       = nota;
        this.comentario = comentario;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Feedback{id=" + id
                + ", usuarioId=" + usuarioId
                + ", produtoId=" + produtoId
                + ", nota=" + nota
                + ", comentario='" + comentario + "'"
                + ", dataCriacao=" + dataCriacao + "}";
    }
}
