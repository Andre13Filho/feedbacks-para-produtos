package br.edu.faculdade.feedback.service;

import br.edu.faculdade.feedback.model.dao.ProdutoDAO;
import br.edu.faculdade.feedback.model.entity.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void cadastrar(String nome, String descricao) throws IllegalArgumentException, SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        Produto produto = new Produto(nome.trim(), descricao != null ? descricao.trim() : null);
        produtoDAO.inserir(produto);
    }

    public List<Produto> listar() throws SQLException {
        return produtoDAO.listarTodos();
    }

    public Produto buscarPorId(int id) throws SQLException {
        return produtoDAO.buscarPorId(id);
    }
}
