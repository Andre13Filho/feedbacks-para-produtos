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

/**
 * Controller REST para operações com Usuários.
 * Responsável pelo endpoint de login (autenticação).
 *
 * Rotas:
 *   POST /api/login     → valida e-mail e senha, retorna dados do usuário
 *   POST /api/usuarios  → cadastra um novo usuário
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
@WebServlet("/api/usuarios/*")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        // POST /api/usuarios/login → autenticação
        if (pathInfo != null && "/login".equals(pathInfo)) {
            login(req, resp);
        } else {
            // POST /api/usuarios → cadastro
            cadastrar(req, resp);
        }
    }

    /**
     * POST /api/usuarios/login
     * Body JSON esperado: { "email": "...", "senha": "..." }
     *
     * Valida as credenciais e retorna os dados do usuário.
     * Essa rota prepara o terreno para o André implementar o JWT:
     * futuramente, ao invés de retornar só o usuário, retornará
     * também o token JWT gerado.
     */
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Lê e-mail e senha do body JSON
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            String email = body.has("email") ? body.get("email").getAsString() : null;
            String senha = body.has("senha") ? body.get("senha").getAsString() : null;

            // Valida no Service e busca no banco
            Usuario usuario = usuarioService.login(email, senha);

            // Monta resposta com os dados do usuário (sem expor a senha)
            JsonObject resposta = new JsonObject();
            resposta.addProperty("mensagem", "Login realizado com sucesso!");
            resposta.addProperty("id", usuario.getId());
            resposta.addProperty("nome", usuario.getNome());
            resposta.addProperty("email", usuario.getEmail());
            // Aqui o André poderá adicionar: resposta.addProperty("token", tokenJWT);

            JsonUtil.enviarResposta(resp, 200, resposta);
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 401, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno ao realizar login.");
        }
    }

    /**
     * POST /api/usuarios
     * Body JSON esperado: { "nome": "...", "email": "...", "senha": "..." }
     */
    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            String nome  = body.has("nome")  ? body.get("nome").getAsString()  : null;
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
