<div class="container">
    <h1>Deixe seu Feedback</h1>
    <form method="post" action="<%= ctx %>/feedback/salvar">
        <input type="hidden" name="produtoId" value="<%= request.getParameter(" id") %>">

        <div class="field">
            <label>Nota (1 a 5):</label>
            <input type="number" name="nota" min="1" max="5" required>
        </div>

        <div class="field">
            <label>Comentário:</label>
            <textarea name="comentario" rows="4" style="width: 100%;" required></textarea>
        </div>

        <div class="actions">
            <button type="submit" class="btn">Enviar Avaliação</button>
            <a href="<%= ctx %>/produto/listar">Cancelar</a>
        </div>
    </form>
</div>