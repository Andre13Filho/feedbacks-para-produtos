package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FeedbackDAO {


    public void inserir(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO Feedback (usuario_id, produto_id, nota, comentario) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, feedback.getUsuarioId());
            stmt.setInt(2, feedback.getProdutoId());
            stmt.setInt(3, feedback.getNota());
            stmt.setString(4, feedback.getComentario());
            stmt.executeUpdate();

            
            try (ResultSet chaveGerada = stmt.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    feedback.setId(chaveGerada.getInt(1));
                }
            }
        }
    }

   
    public List<Feedback> listarPorProduto(int produtoId) throws SQLException {
        String sql = "SELECT id, usuario_id, produto_id, nota, comentario, data_criacao "
                   + "FROM Feedback "
                   + "WHERE produto_id = ? "
                   + "ORDER BY data_criacao DESC";

        List<Feedback> feedbacks = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbacks.add(mapearResultado(rs));
                }
            }
        }
        return feedbacks;
    }


    private Feedback mapearResultado(ResultSet rs) throws SQLException {
        return new Feedback(
            rs.getInt("id"),
            rs.getInt("usuario_id"),
            rs.getInt("produto_id"),
            rs.getInt("nota"),
            rs.getString("comentario"),
            rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }
}
