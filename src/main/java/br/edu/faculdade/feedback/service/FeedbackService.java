package br.edu.faculdade.feedback.service;

import br.edu.faculdade.feedback.model.dao.FeedbackDAO;
import br.edu.faculdade.feedback.model.dao.ProdutoDAO;
import br.edu.faculdade.feedback.model.dao.UsuarioDAO;
import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.model.entity.Usuario;

import java.sql.SQLException;
import java.util.List;

public class FeedbackService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public Feedback registrarFeedback(int usuarioId, int produtoId, int nota, String comentario)
            throws IllegalArgumentException, SQLException {

        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 1 e 5. Recebido: " + nota);
        }
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("O comentário não pode ser vazio.");
        }

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + usuarioId);
        }

        Produto produto = produtoDAO.buscarPorId(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com o ID: " + produtoId);
        }

        Feedback feedback = new Feedback(usuarioId, produtoId, nota, comentario.trim());
        feedbackDAO.inserir(feedback);
        return feedback;
    }

    public List<Feedback> listarFeedbacksDoProduto(int produtoId) throws SQLException {
        return feedbackDAO.listarPorProduto(produtoId);
    }

    public double calcularMediaAvaliacoes(List<Feedback> feedbacks) {
        if (feedbacks == null || feedbacks.isEmpty()) return 0.0;
        double soma = 0;
        for (Feedback f : feedbacks) {
            soma += f.getNota();
        }
        return soma / feedbacks.size();
    }

    public Feedback buscarPorId(int id) throws SQLException {
        return feedbackDAO.buscarPorId(id);
    }

    public void atualizar(int feedbackId, int nota, String comentario)
            throws IllegalArgumentException, SQLException {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 1 e 5. Recebido: " + nota);
        }
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("O comentário não pode ser vazio.");
        }

        Feedback feedback = feedbackDAO.buscarPorId(feedbackId);
        if (feedback == null) {
            throw new IllegalArgumentException("Feedback não encontrado com o ID: " + feedbackId);
        }
        feedback.setNota(nota);
        feedback.setComentario(comentario.trim());
        feedbackDAO.atualizar(feedback);
    }

    public void deletar(int feedbackId) throws IllegalArgumentException, SQLException {
        Feedback feedback = feedbackDAO.buscarPorId(feedbackId);
        if (feedback == null) {
            throw new IllegalArgumentException("Feedback não encontrado com o ID: " + feedbackId);
        }
        feedbackDAO.deletar(feedbackId);
    }
}
