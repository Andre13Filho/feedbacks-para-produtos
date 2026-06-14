package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.service.FeedbackService;
import br.edu.faculdade.feedback.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller REST para operações com Feedbacks.
 * Recebe e retorna dados em JSON.
 *
 * Rotas:
 *   GET  /api/feedbacks?produtoId=1  → lista feedbacks de um produto
 *   POST /api/feedbacks              → registra um novo feedback
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
@WebServlet("/api/feedbacks/*")
public class FeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();

    /**
     * GET /api/feedbacks?produtoId=1
     * Retorna todos os feedbacks de um produto específico.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String produtoIdParam = req.getParameter("produtoId");

        if (produtoIdParam == null || produtoIdParam.isEmpty()) {
            JsonUtil.enviarErro(resp, 400, "O parâmetro 'produtoId' é obrigatório.");
            return;
        }

        try {
            int produtoId = Integer.parseInt(produtoIdParam);
            List<Feedback> feedbacks = feedbackService.listarFeedbacksDoProduto(produtoId);
            JsonUtil.enviarResposta(resp, 200, feedbacks);
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "produtoId inválido. Informe um número inteiro.");
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao buscar feedbacks.");
        }
    }

    /**
     * POST /api/feedbacks
     * Body JSON esperado: { "usuarioId": 1, "produtoId": 2, "nota": 5, "comentario": "..." }
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Lê o JSON do body e converte para objeto Feedback
            Feedback dados = JsonUtil.lerBody(req, Feedback.class);

            // Chama o Service que valida as regras e salva no banco
            Feedback salvo = feedbackService.registrarFeedback(
                    dados.getUsuarioId(),
                    dados.getProdutoId(),
                    dados.getNota(),
                    dados.getComentario()
            );

            JsonUtil.enviarResposta(resp, 201, salvo);
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao registrar feedback.");
        }
    }
}
