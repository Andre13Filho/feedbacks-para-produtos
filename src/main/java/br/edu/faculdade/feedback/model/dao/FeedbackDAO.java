package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da entidade Feedback.
 * Responsável pelas operações SQL na tabela Feedback.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class FeedbackDAO {

    /**
     * Insere um novo feedback no banco de dados.
     * A data_criacao é gerada automaticamente pelo MySQL.
     */
    public void inserir(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO Feedback (usuario_id, produto_id, nota, comentario) "
                   + "VALUES (?, ?, ?, ?)";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    /**
     * Lista todos os feedbacks de um produto, do mais recente ao mais antigo.
     */
    public List<Feedback> listarPorProduto(int produtoId) throws SQLException {
        String sql = "SELECT id, usuario_id, produto_id, nota, comentario, data_criacao "
                   + "FROM Feedback "
                   + "WHERE produto_id = ? "
                   + "ORDER BY data_criacao DESC";

        List<Feedback> feedbacks = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbacks.add(mapearResultado(rs));
                }
            }
        }
        return feedbacks;
    }

    /**
     * Converte uma linha do ResultSet em objeto Feedback.
     */
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
