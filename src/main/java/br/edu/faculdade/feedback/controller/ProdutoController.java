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

@WebServlet("/produto/*")
public class ProdutoController extends HttpServlet {

    private final ProdutoService produtoService = new ProdutoService();
    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);

        switch (rota) {
            case "/listar": listar(req, resp); break;
            case "/avaliar": avaliar(req, resp); break;
            case "/novo": novo(req, resp); break;
            case "/detalhes": detalhes(req, resp); break;
            default: resp.sendRedirect(req.getContextPath() + "/produto/listar"); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if ("PUT".equalsIgnoreCase(method)) { doPut(req, resp); return; }
        if ("DELETE".equalsIgnoreCase(method)) { doDelete(req, resp); return; }

        String rota = extrairRota(req);
        if ("/cadastrar".equals(rota)) {
            cadastrar(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);
        if ("/atualizar".equals(rota)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String nome = req.getParameter("nome");
                String descricao = req.getParameter("descricao");
                produtoService.atualizar(id, nome, descricao);
                resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso_atualizacao=true");
            } catch (IllegalArgumentException e) {
                resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
            } catch (SQLException e) {
                throw new ServletException("Erro ao atualizar produto", e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);
        if ("/deletar".equals(rota)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                produtoService.deletar(id);
                resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso_delecao=true");
            } catch (IllegalArgumentException e) {
                resp.sendRedirect(req.getContextPath() + "/produto/listar?erro=" + resp.encodeRedirectURL(e.getMessage()));
            } catch (SQLException e) {
                throw new ServletException("Erro ao deletar produto", e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private String extrairRota(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) return "/listar";
        return pathInfo;
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Produto> produtos = produtoService.listar();
            req.setAttribute("produtos", produtos);
            req.getRequestDispatcher("/lista_produtos.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar a lista de produtos", e);
        }
    }

    private void avaliar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Produto produto = produtoService.buscarPorId(id);
                if (produto == null) {
                    JsonUtil.enviarErro(resp, 404, "Produto não encontrado com o ID: " + id);
                    return;
                }
            } catch (SQLException e) {
            }
        }
        resp.sendRedirect(req.getContextPath() + "/produto/listar");
    }

    private void novo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/form_produto.jsp").forward(req, resp);
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String nome = req.getParameter("nome");
            String descricao = req.getParameter("descricao");
            produtoService.cadastrar(nome, descricao);
            resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/produto/novo?erro=" + resp.encodeRedirectURL(e.getMessage()));
        } catch (SQLException e) {
            throw new ServletException("Erro ao cadastrar produto", e);
        }
    }

    private void detalhes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Produto produto = produtoService.buscarPorId(id);
                if (produto != null) {
                    List<Feedback> feedbacks = feedbackService.listarFeedbacksDoProduto(id);
                    double media = feedbackService.calcularMediaAvaliacoes(feedbacks);
                    req.setAttribute("produto", produto);
                    req.setAttribute("feedbacks", feedbacks);
                    req.setAttribute("media", media);
                    req.getRequestDispatcher("/detalhes_produto.jsp").forward(req, resp);
                    return;
                }
            } catch (SQLException e) {
            }
        }
        resp.sendRedirect(req.getContextPath() + "/produto/listar");
    }
}
