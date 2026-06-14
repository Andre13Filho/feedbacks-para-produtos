package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.service.FeedbackService;
import br.edu.faculdade.feedback.service.ProdutoService;
import br.edu.faculdade.feedback.util.JsonUtil;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller REST para operações com Produtos.
 * Recebe requisições HTTP, lê os dados do body em JSON
 * e retorna as respostas também em JSON.
 *
 * Rotas:
 *   GET  /api/produtos          → lista todos os produtos
 *   GET  /api/produtos/1        → busca produto por ID (com feedbacks)
 *   POST /api/produtos          → cadastra um produto novo
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
@WebServlet("/api/produtos/*")
public class ProdutoController extends HttpServlet {

    private final ProdutoService produtoService = new ProdutoService();
    private final FeedbackService feedbackService = new FeedbackService();

    /**
     * GET /api/produtos      → lista todos
     * GET /api/produtos/1    → busca por ID com feedbacks e média
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            // Se não veio ID na URL, lista todos os produtos
            if (pathInfo == null || "/".equals(pathInfo)) {
                List<Produto> produtos = produtoService.listar();
                JsonUtil.enviarResposta(resp, 200, produtos);
            } else {
                // Extrai o ID da URL (ex: /api/produtos/3 → "3")
                int id = Integer.parseInt(pathInfo.substring(1));
                Produto produto = produtoService.buscarPorId(id);

                if (produto == null) {
                    JsonUtil.enviarErro(resp, 404, "Produto não encontrado com o ID: " + id);
                    return;
                }

                // Monta a resposta com produto + feedbacks + média
                List<Feedback> feedbacks = feedbackService.listarFeedbacksDoProduto(id);
                double media = feedbackService.calcularMediaAvaliacoes(feedbacks);

                JsonObject json = new JsonObject();
                json.add("produto", JsonUtil.getGson().toJsonTree(produto));
                json.add("feedbacks", JsonUtil.getGson().toJsonTree(feedbacks));
                json.addProperty("mediaAvaliacoes", media);
                json.addProperty("totalFeedbacks", feedbacks.size());

                JsonUtil.enviarResposta(resp, 200, json);
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido. Informe um número inteiro.");
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao acessar o banco de dados.");
        }
    }

    /**
     * POST /api/produtos
     * Body JSON esperado: { "nome": "...", "descricao": "..." }
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Lê o JSON do body da requisição e converte para objeto Produto
            Produto dados = JsonUtil.lerBody(req, Produto.class);

            produtoService.cadastrar(dados.getNome(), dados.getDescricao());

            JsonUtil.enviarSucesso(resp, 201, "Produto cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao cadastrar produto.");
        }
    }
}
