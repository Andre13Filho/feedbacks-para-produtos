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

    public void atualizar(int id, String nome, String descricao) throws IllegalArgumentException, SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com o ID: " + id);
        }
        produto.setNome(nome.trim());
        produto.setDescricao(descricao != null ? descricao.trim() : null);
        produtoDAO.atualizar(produto);
    }

    public void deletar(int id) throws IllegalArgumentException, SQLException {
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com o ID: " + id);
        }
        produtoDAO.deletar(id);
    }
}
