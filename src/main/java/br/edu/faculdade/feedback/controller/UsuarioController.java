package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.service.UsuarioService;
import br.edu.faculdade.feedback.util.JsonUtil;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/usuarios/*")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && "/login".equals(pathInfo)) {
            login(req, resp);
        } else {
            cadastrar(req, resp);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            String email = body.has("email") ? body.get("email").getAsString() : null;
            String senha = body.has("senha") ? body.get("senha").getAsString() : null;

            Usuario usuario = usuarioService.login(email, senha);

            JsonObject resposta = new JsonObject();
            resposta.addProperty("mensagem", "Login realizado com sucesso!");
            resposta.addProperty("id", usuario.getId());
            resposta.addProperty("nome", usuario.getNome());
            resposta.addProperty("email", usuario.getEmail());

            JsonUtil.enviarResposta(resp, 200, resposta);
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 401, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao realizar login.");
        }
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            String nome = body.has("nome") ? body.get("nome").getAsString() : null;
            String email = body.has("email") ? body.get("email").getAsString() : null;
            String senha = body.has("senha") ? body.get("senha").getAsString() : null;

            Usuario usuario = usuarioService.cadastrar(nome, email, senha);

            JsonObject resposta = new JsonObject();
            resposta.addProperty("mensagem", "Usuário cadastrado com sucesso!");
            resposta.addProperty("id", usuario.getId());
            resposta.addProperty("nome", usuario.getNome());
            resposta.addProperty("email", usuario.getEmail());

            JsonUtil.enviarResposta(resp, 201, resposta);
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao cadastrar usuário.");
        }
    }
}
