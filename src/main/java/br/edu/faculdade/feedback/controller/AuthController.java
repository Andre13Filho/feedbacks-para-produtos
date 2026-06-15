package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.dao.UsuarioDAO;
import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.util.JwtUtil;
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

@WebServlet("/api/login")
public class AuthController extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            JsonObject payload = gson.fromJson(reader, JsonObject.class);

            if (payload == null || !payload.has("email") || !payload.has("senha")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Email e senha são obrigatórios\"}");
                return;
            }

            String email = payload.get("email").getAsString();
            String senha = payload.get("senha").getAsString();

            Usuario usuario = usuarioDAO.buscarPorEmailESenha(email, senha);

            if (usuario == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Credenciais inválidas\"}");
                return;
            }

            String token = JwtUtil.generateToken(usuario);

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("token", token);
            responseJson.addProperty("userId", usuario.getId());
            responseJson.addProperty("nome", usuario.getNome());

            resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(responseJson));
            out.flush();

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Erro interno do servidor\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Erro ao processar a requisição\"}");
        }
    }
}
