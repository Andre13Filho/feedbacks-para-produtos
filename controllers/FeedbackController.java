@WebServlet("/feedback/*")
public class FeedbackController extends HttpServlet {

    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Captura os dados do formulário [cite: 1, 102-110]
        Long produtoId = Long.parseLong(req.getParameter("produtoId"));
        int nota = Integer.parseInt(req.getParameter("nota"));
        String comentario = req.getParameter("comentario");

        // Simulação de usuário logado (conforme o exemplo de sessão do material)
        Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");

        Feedback fb = new Feedback();
        fb.setProdutoId(produtoId);
        fb.setUsuarioId(usuarioLogado.getId());
        fb.setNota(nota);
        fb.setComentario(comentario);

        feedbackService.salvar(fb);

        resp.sendRedirect(req.getContextPath() + "/produto/listar?sucesso=true");
    }
}