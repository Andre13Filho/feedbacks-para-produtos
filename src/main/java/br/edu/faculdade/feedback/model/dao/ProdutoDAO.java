package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da entidade Produto.
 * Responsável pelas operações SQL na tabela Produtos.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     */
    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome, descricao) VALUES (?, ?)";

        // Obtém a conexão Singleton (não pode fechar ela no try-with-resources)
        Connection conn = ConnectionFactory.getConnection();

        // Apenas o PreparedStatement é fechado automaticamente
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.executeUpdate();

            try (ResultSet chaveGerada = stmt.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    produto.setId(chaveGerada.getInt(1));
                }
            }
        }
    }

    /**
     * Retorna todos os produtos cadastrados, ordenados por nome.
     */
    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, descricao FROM Produtos ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearResultado(rs));
            }
        }
        return produtos;
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @return Produto encontrado ou null se não existir
     */
    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, descricao FROM Produtos WHERE id = ?";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        return null;
    }

    /**
     * Converte uma linha do ResultSet em objeto Produto.
     */
    private Produto mapearResultado(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("descricao")
        );
    }
}
