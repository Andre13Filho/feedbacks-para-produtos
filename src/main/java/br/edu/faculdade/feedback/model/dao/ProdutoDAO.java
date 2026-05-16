package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO {


    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome, descricao) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, descricao FROM Produtos ORDER BY nome";

        
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            
            while (rs.next()) {
                produtos.add(mapearResultado(rs));
            }
        }
        return produtos;
    }


    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, descricao FROM Produtos WHERE id = ?";

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


    private Produto mapearResultado(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("descricao")
        );
    }
}
