package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;

public class UsuarioDAO {

    public void criar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM Usuarios WHERE id = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultado(rs);
            }
        }
        return null;
    }

    public Usuario buscarPorEmailESenha(String email, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM Usuarios WHERE email = ? AND senha = ?";
        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultado(rs);
            }
        }
        return null;
    }

    private Usuario mapearResultado(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("senha")
        );
    }
}
