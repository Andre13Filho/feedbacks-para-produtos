package br.edu.faculdade.feedback.service;

import br.edu.faculdade.feedback.model.dao.ProdutoDAO;
import br.edu.faculdade.feedback.model.entity.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public List<Produto> listar() throws SQLException {
        return produtoDAO.listarTodos();
    }

    public Produto buscarPorId(int id) throws SQLException {
        return produtoDAO.buscarPorId(id);
    }
}
