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
        try {
            // Captura os dados do formulário [cite: 1, 102-110]
            int produtoId = Integer.parseInt(req.getParameter("produtoId"));
            int nota = Integer.parseInt(req.getParameter("nota"));
            String comentario = req.getParameter("comentario");

            // Simulação de usuário logado (conforme o exemplo de sessão do material)
            Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            
            // Define um usuário de fallback seguro caso a sessão ainda não possua o objeto logado
            int usuarioId = 1;
            if (usuarioLogado != null) {
                usuarioId = usuarioLogado.getId();
            }

            // Utiliza o método correto do serviço que aplica regras de negócio e salva o feedback
            feedbackService.registrarFeedback(usuarioId, produtoId, nota, comentario);

            resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
        } catch (SQLException e) {
            throw new ServletException("Erro ao processar o feedback no banco de dados", e);
        }
    }
}
