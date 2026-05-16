<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.edu.faculdade.feedback.model.entity.Produto" %>
<%
    String ctx = request.getContextPath();
    Produto produto = (Produto) request.getAttribute("produto");
    String produtoId = request.getParameter("id");
    if (produtoId == null && produto != null) {
        produtoId = String.valueOf(produto.getId());
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Deixe seu Feedback</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            padding: 24px;
            margin: 0;
            color: #333;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background: #fff;
            padding: 32px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        h1 {
            margin-top: 0;
            color: #1a73e8;
            font-size: 26px;
            margin-bottom: 20px;
            border-bottom: 2px solid #e8eaed;
            padding-bottom: 12px;
        }

        .product-info {
            background-color: #e8f0fe;
            padding: 14px 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            color: #1a73e8;
            font-weight: 600;
            border: 1px solid #d2e3fc;
            font-size: 15px;
        }

        .field {
            margin-bottom: 20px;
        }

        .field label {
            display: block;
            font-weight: 600;
            margin-bottom: 8px;
            color: #4c5156;
            font-size: 14px;
        }

        .field input[type="number"], .field textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #dadce0;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 15px;
            transition: border-color 0.2s, box-shadow 0.2s;
            font-family: inherit;
        }

        .field input[type="number"]:focus, .field textarea:focus {
            outline: none;
            border-color: #1a73e8;
            box-shadow: 0 0 0 3px rgba(26, 115, 232, 0.2);
        }

        .actions {
            margin-top: 28px;
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .btn {
            padding: 10px 24px;
            background: #1a73e8;
            color: #fff;
            border: none;
            cursor: pointer;
            font-size: 15px;
            font-weight: 500;
            border-radius: 6px;
            transition: background 0.2s;
        }

        .btn:hover {
            background: #1557b0;
        }

        .cancel-link {
            color: #d32f2f;
            text-decoration: none;
            font-size: 15px;
            font-weight: 500;
            transition: color 0.2s;
        }

        .cancel-link:hover {
            color: #b71c1c;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Deixe seu Feedback</h1>
        
        <% if (produto != null) { %>
            <div class="product-info">
                ⭐ Avaliando o produto: <%= produto.getNome() %>
            </div>
        <% } %>

        <form method="post" action="<%= ctx %>/feedback/salvar">
            <input type="hidden" name="produtoId" value="<%= produtoId != null ? produtoId : "" %>">

            <div class="field">
                <label for="nota">Nota (1 a 5):</label>
                <input type="number" id="nota" name="nota" min="1" max="5" placeholder="Insira uma nota de 1 a 5" required>
            </div>

            <div class="field">
                <label for="comentario">Comentário:</label>
                <textarea id="comentario" name="comentario" rows="5" placeholder="Escreva aqui o que você achou do produto..." required></textarea>
            </div>

            <div class="actions">
                <button type="submit" class="btn">Enviar Avaliação</button>
                <a href="<%= ctx %>/produto/listar" class="cancel-link">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>
