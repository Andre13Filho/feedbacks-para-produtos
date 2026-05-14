package br.edu.faculdade.feedback.model.entity;


public class Produto {

    private int    id;
    private String nome;
    private String descricao;



    public Produto() {}


    public Produto(int id, String nome, String descricao) {
        this.id        = id;
        this.nome      = nome;
        this.descricao = descricao;
    }


    public Produto(String nome, String descricao) {
        this.nome      = nome;
        this.descricao = descricao;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + "', descricao='" + descricao + "'}";
    }
}
