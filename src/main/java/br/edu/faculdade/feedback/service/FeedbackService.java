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

    // Instâncias dos DAOs que este serviço precisa utilizar
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final UsuarioDAO  usuarioDAO  = new UsuarioDAO();
    private final ProdutoDAO  produtoDAO  = new ProdutoDAO();

    /**
     * @param usuarioId  ID do usuário que está avaliando
     * @param produtoId  ID do produto sendo avaliado
     * @param nota       nota de 1 a 5
     * @param comentario texto da avaliação
     * @return objeto {@link Feedback} salvo, já com o ID gerado pelo banco
     * @throws IllegalArgumentException se alguma regra de negócio for violada
     * @throws SQLException             se ocorrer erro ao acessar o banco
     */
    public Feedback registrarFeedback(int usuarioId, int produtoId,
                                      int nota, String comentario)
            throws IllegalArgumentException, SQLException {

        // --- Validação 1: nota entre 1 e 5 ---
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException(
                "A nota deve ser um valor entre 1 e 5. Valor recebido: " + nota);
        }

        // --- Validação 2: comentário obrigatório ---
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("O comentário não pode ser vazio.");
        }

        // --- Validação 3: usuário deve existir ---
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException(
                "Usuário não encontrado com o ID: " + usuarioId);
        }

        // --- Validação 4: produto deve existir ---
        Produto produto = produtoDAO.buscarPorId(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado com o ID: " + produtoId);
        }

        // Todas as validações passaram: cria e persiste o feedback
        Feedback feedback = new Feedback(usuarioId, produtoId, nota, comentario.trim());
        feedbackDAO.inserir(feedback); // após este ponto, feedback.getId() estará preenchido

        return feedback;
    }


    public List<Feedback> listarFeedbacksDoProduto(int produtoId) throws SQLException {
        return feedbackDAO.listarPorProduto(produtoId);
    }
}
