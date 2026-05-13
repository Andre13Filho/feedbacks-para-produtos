@WebServlet("/produto/*")
public class ProdutoController extends HttpServlet {

    private static final String PREFIXO_APP = "/produto";
    private final ProdutoService produtoService = new ProdutoService(); // Assumindo a criação pelo seu colega

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rota = extrairRota(req);

        switch (rota) {
            case "/listar":
                listar(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + PREFIXO_APP + "/listar");
                break;
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
        List<Produto> produtos = produtoService.listar(); // [cite: 91-96]
        req.setAttribute("produtos", produtos);
        req.getRequestDispatcher("/produto/lista_produtos.jsp").forward(req, resp);
    }
}