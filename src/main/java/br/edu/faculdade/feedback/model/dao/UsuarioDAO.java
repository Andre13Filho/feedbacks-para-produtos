package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;


public class UsuarioDAO {


    public void criar(Usuario usuario) throws SQLException {
        // RETURN_GENERATED_KEYS permite recuperar o ID gerado pelo AUTO_INCREMENT
        String sql = "INSERT INTO Usuarios (nome, email) VALUES (?, ?)";

        // try-with-resources: fecha a conexão e o statement automaticamente
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.executeUpdate();

            // Recupera o ID que o banco gerou e coloca no objeto
            try (ResultSet chaveGerada = stmt.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    usuario.setId(chaveGerada.getInt(1));
                }
            }
        }
    }


    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email FROM Usuarios WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        
        return null;
    }


    private Usuario mapearResultado(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("email")
        );
    }
}
