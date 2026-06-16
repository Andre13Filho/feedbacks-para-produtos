package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.service.FeedbackService;
import br.edu.faculdade.feedback.service.ProdutoService;
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

@WebServlet("/api/produtos/*")
public class ProdutoRestController extends HttpServlet {

    private final ProdutoService produtoService = new ProdutoService();
    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/produtos (Lista todos)
                List<Produto> produtos = produtoService.listar();
                JsonUtil.enviarResposta(resp, 200, produtos);
            } else {
                // GET /api/produtos/{id}
                String[] parts = pathInfo.split("/");
                if (parts.length > 1) {
                    int id = Integer.parseInt(parts[1]);
                    Produto produto = produtoService.buscarPorId(id);

                    if (produto != null) {
                        List<Feedback> feedbacks = feedbackService.listarFeedbacksDoProduto(id);
                        double media = feedbackService.calcularMediaAvaliacoes(feedbacks);

                        JsonObject responseObj = new JsonObject();
                        responseObj.add("produto", JsonUtil.getGson().toJsonTree(produto));
                        responseObj.add("feedbacks", JsonUtil.getGson().toJsonTree(feedbacks));
                        responseObj.addProperty("media", media);

                        JsonUtil.enviarResposta(resp, 200, responseObj);
                    } else {
                        JsonUtil.enviarErro(resp, 404, "Produto não encontrado com o ID: " + id);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido.");
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro interno no banco de dados.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // POST /api/produtos (Cadastrar novo)
            JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
            if (body == null || !body.has("nome") || !body.has("descricao")) {
                JsonUtil.enviarErro(resp, 400, "Obrigatório enviar 'nome' e 'descricao' no formato JSON.");
                return;
            }

            String nome = body.get("nome").getAsString();
            String descricao = body.get("descricao").getAsString();

            produtoService.cadastrar(nome, descricao);
            JsonUtil.enviarSucesso(resp, 201, "Produto cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao cadastrar produto.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            // PUT /api/produtos/{id}
            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
                
                if (body == null || !body.has("nome") || !body.has("descricao")) {
                    JsonUtil.enviarErro(resp, 400, "Obrigatório enviar 'nome' e 'descricao'.");
                    return;
                }

                String nome = body.get("nome").getAsString();
                String descricao = body.get("descricao").getAsString();

                produtoService.atualizar(id, nome, descricao);
                JsonUtil.enviarSucesso(resp, 200, "Produto atualizado com sucesso!");
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do produto não informado na URL.");
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido.");
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao atualizar produto.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            // DELETE /api/produtos/{id}
            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                produtoService.deletar(id);
                resp.setStatus(204);
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do produto não informado na URL.");
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido.");
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao deletar produto.");
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
                int id = Integer.parseInt(pathInfo.substring(1));
                JsonObject body = JsonUtil.lerBody(req, JsonObject.class);
                
                Produto produto = produtoService.buscarPorId(id);
                if (produto == null) {
                    JsonUtil.enviarErro(resp, 404, "Produto não encontrado com o ID: " + id);
                    return;
                }

                String nome = body.has("nome") ? body.get("nome").getAsString() : produto.getNome();
                String descricao = body.has("descricao") ? body.get("descricao").getAsString() : produto.getDescricao();

                produtoService.atualizar(id, nome, descricao);
                JsonUtil.enviarSucesso(resp, 200, "Produto atualizado com sucesso!");
            } else {
                JsonUtil.enviarErro(resp, 400, "ID do produto não informado na URL.");
            }
        } catch (NumberFormatException e) {
            JsonUtil.enviarErro(resp, 400, "ID inválido.");
        } catch (IllegalArgumentException e) {
            JsonUtil.enviarErro(resp, 400, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.enviarErro(resp, 500, "Erro ao atualizar produto.");
        }
    }
}
