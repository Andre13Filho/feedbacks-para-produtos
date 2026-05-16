package br.edu.faculdade.feedback.controller;

import br.edu.faculdade.feedback.model.entity.Feedback;
import br.edu.faculdade.feedback.model.entity.Produto;
import br.edu.faculdade.feedback.service.FeedbackService;
import br.edu.faculdade.feedback.service.ProdutoService;

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

    private static final String PREFIXO_APP = "/produto";
    private final ProdutoService produtoService = new ProdutoService(); // Instância criada corretamente
    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);

        switch (rota) {
            case "/listar":
                listar(req, resp);
                break;
            case "/avaliar":
                avaliar(req, resp);
                break;
            case "/novo":
                novo(req, resp);
                break;
            case "/detalhes":
                detalhes(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);
        if ("/cadastrar".equals(rota)) {
            cadastrar(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private String extrairRota(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            return "/listar";
        }
        return pathInfo;
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Produto> produtos = produtoService.listar(); // [cite: 91-96]
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
                if (produto != null) {
                    req.setAttribute("produto", produto);
                    req.getRequestDispatcher("/form-feedback.jsp").forward(req, resp);
                    return;
                }
            } catch (SQLException | NumberFormatException e) {
                // Em caso de id inválido ou erro, prossegue para redirecionar à listagem
            }
        }
        resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/listar");
    }

    private void novo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/form_produto.jsp").forward(req, resp);
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String descricao = req.getParameter("descricao");

        try {
            produtoService.cadastrar(nome, descricao);
            resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/listar?sucesso_produto=true");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/novo?erro=" + resp.encodeRedirectURL(e.getMessage()));
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
            } catch (SQLException | NumberFormatException e) {
                // Em caso de id inválido ou erro, prossegue para redirecionar à listagem
            }
        }
        resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/listar");
    }
}
