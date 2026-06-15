package br.edu.faculdade.feedback.controller.api;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.service.FeedbackService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/feedbacks/*")
public class ApiFeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            JsonObject payload = gson.fromJson(reader, JsonObject.class);

            if (payload == null || !payload.has("produtoId") || !payload.has("nota") || !payload.has("comentario")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Dados incompletos\"}");
                return;
            }

            int produtoId = payload.get("produtoId").getAsInt();
            int nota = payload.get("nota").getAsInt();
            String comentario = payload.get("comentario").getAsString();

            Integer usuarioId = (Integer) req.getAttribute("usuarioId");

            if (usuarioId == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Usuário não autenticado\"}");
                return;
            }

            Feedback feedback = feedbackService.registrarFeedback(usuarioId, produtoId, nota, comentario);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(feedback));
            out.flush();

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Erro ao processar o feedback\"}");
        }
    }
}
