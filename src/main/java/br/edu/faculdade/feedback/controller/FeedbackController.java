package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.service.FeedbackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/feedback/*")
public class FeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if ("PUT".equalsIgnoreCase(method)) {
            doPut(req, resp);
            return;
        } else if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(req, resp);
            return;
        }

        try {
            int produtoId = Integer.parseInt(req.getParameter("produtoId"));
            int nota = Integer.parseInt(req.getParameter("nota"));
            String comentario = req.getParameter("comentario");

            Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");

            int usuarioId = 1;
            if (usuarioLogado != null) {
                usuarioId = usuarioLogado.getId();
            }

            feedbackService.registrarFeedback(usuarioId, produtoId, nota, comentario);

            resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
        } catch (SQLException e) {
            throw new ServletException("Erro ao processar o feedback no banco de dados", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int feedbackId = Integer.parseInt(req.getParameter("id"));
            int nota = Integer.parseInt(req.getParameter("nota"));
            String comentario = req.getParameter("comentario");

            feedbackService.atualizar(feedbackId, nota, comentario);

            resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso_atualizacao=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Erro ao atualizar o feedback no banco de dados", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int feedbackId = Integer.parseInt(req.getParameter("id"));
            feedbackService.deletar(feedbackId);

            resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso_delecao=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Erro ao deletar o feedback no banco de dados", e);
        }
    }
}
