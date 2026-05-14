package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO {


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
