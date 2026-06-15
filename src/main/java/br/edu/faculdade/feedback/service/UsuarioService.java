package br.edu.faculdade.feedback.service;

import br.edu.faculdade.feedback.model.dao.UsuarioDAO;
import br.edu.faculdade.feedback.model.entity.Usuario;

import java.sql.SQLException;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String email, String senha) throws IllegalArgumentException, SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        Usuario usuario = usuarioDAO.buscarPorEmailESenha(email.trim(), senha);
        if (usuario == null) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }
        return usuario;
    }

    public Usuario cadastrar(String nome, String email, String senha)
            throws IllegalArgumentException, SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        Usuario usuario = new Usuario(nome.trim(), email.trim(), senha);
        usuarioDAO.criar(usuario);
        return usuario;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        return usuarioDAO.buscarPorId(id);
    }
}
