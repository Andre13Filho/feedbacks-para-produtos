package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.service.FeedbackService;
import br.edu.faculdade.feedback.util.JsonUtil;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/feedbacks/*")
public class FeedbackRestController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String produtoIdParam = req.getParameter("produtoId");
        
        if (produtoIdParam != null && !produtoIdParam.isEmpty()) {
            try {
                int produtoId = Integer.parseInt(produtoIdParam);
                List<Feedback> feedbacks = feedbackService.listarFeedbacksDoProduto(produtoId);
                JsonUtil.enviarResposta(resp, 200, feedbacks);
            } catch (NumberFormatException e) {
                JsonUtil.enviarErro(resp, 400, "produtoId inválido.");
            } catch (SQLException e) {
                JsonUtil.enviarErro(resp, 500, "Erro ao buscar feedbacks.");
            }
        } else {
            JsonUtil.enviarErro(resp, 400, "O parâmetro produtoId é obrigatório. Ex: /api/feedbacks?produtoId=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            if (body == null || !body.has("produtoId") || !body.has("nota") || !body.has("comentario")) {
                JsonUtil.enviarErro(resp, 400, "Obrigatório enviar 'produtoId', 'nota' e 'comentario' no body.");
                return;
            }

            int produtoId = body.get("produtoId").getAsInt();
            int nota = body.get("nota").getAsInt();
            String comentario = body.get("comentario").getAsString();

            Integer usuarioId = (Integer) req.getAttribute("usuarioId");
            if (usuarioId == null) {
                JsonUtil.enviarErro(resp, 401, "Usuário não autenticado.");
                return;
            }

            feedbackService.registrarFeedback(usuarioId, produtoId, nota, comentario);
            JsonUtil.enviarSucesso(resp, 201, "Feedback salvo com sucesso!");

        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao processar o feedback no banco de dados");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                int feedbackId = Integer.parseInt(pathInfo.substring(1));
                JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
                
                if (body == null || !body.has("nota") || !body.has("comentario")) {
                    JsonUtil.enviarErro(resp, 400, "Obrigatório enviar 'nota' e 'comentario' no body.");
                    return;
                }

                int nota = body.get("nota").getAsInt();
                String comentario = body.get("comentario").getAsString();

                feedbackService.atualizar(feedbackId, nota, comentario);
                JsonUtil.enviarSucesso(resp, 200, "Feedback atualizado com sucesso!");
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do feedback não informado na URL.");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao atualizar o feedback no banco de dados.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                int feedbackId = Integer.parseInt(pathInfo.substring(1));
                feedbackService.deletar(feedbackId);
                resp.setStatus(204);
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do feedback não informado na URL.");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao deletar o feedback no banco de dados.");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                int feedbackId = Integer.parseInt(pathInfo.substring(1));
                JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
                
                Feedback feedback = feedbackService.buscarPorId(feedbackId);
                if (feedback == null) {
                    JsonUtil.enviarErro(resp, 404, "Feedback não encontrado com o ID: " + feedbackId);
                    return;
                }

                int nota = body.has("nota") ? body.get("nota").getAsInt() : feedback.getNota();
                String comentario = body.has("comentario") ? body.get("comentario").getAsString() : feedback.getComentario();

                feedbackService.atualizar(feedbackId, nota, comentario);
                JsonUtil.enviarSucesso(resp, 200, "Feedback atualizado com sucesso!");
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do feedback não informado na URL.");
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido.");
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao atualizar o feedback no banco de dados.");
        }
    }
}
