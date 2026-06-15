package br.edu.faculdade.feedback.model.entity;

/**
 * Entidade que representa um usuário do sistema.
 * Cada objeto desta classe corresponde a uma linha na tabela Usuarios.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class Usuario {

    private int    id;
    private String nome;
    private String email;
    private String senha;

    // --- Construtores ---

    public Usuario() {}

    // Construtor completo: usado ao LER do banco
    public Usuario(int id, String nome, String email, String senha) {
        this.id    = id;
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
    }

    // Construtor sem ID: usado ao CRIAR um usuário novo
    public Usuario(String nome, String email, String senha) {
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', email='" + email + "'}";
    }
}
