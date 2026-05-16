<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String erro = request.getParameter("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Produto</title>
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

        .alert-danger {
            background-color: #fce8e6;
            color: #c5221f;
            padding: 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            border: 1px solid #fad2cf;
            font-weight: 500;
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

        .field input[type="text"], .field textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #dadce0;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 15px;
            transition: border-color 0.2s, box-shadow 0.2s;
            font-family: inherit;
        }

        .field input[type="text"]:focus, .field textarea:focus {
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
        <h1>Cadastrar Novo Produto</h1>
        
        <% if (erro != null && !erro.isEmpty()) { %>
            <div class="alert-danger">
                ⚠ Erro: <%= erro %>
            </div>
        <% } %>

        <form method="post" action="<%= ctx %>/produto/cadastrar">
            <div class="field">
                <label for="nome">Nome do Produto:</label>
                <input type="text" id="nome" name="nome" placeholder="Digite o nome do produto" required>
            </div>

            <div class="field">
                <label for="descricao">Descrição:</label>
                <textarea id="descricao" name="descricao" rows="5" placeholder="Descreva os detalhes do produto..."></textarea>
            </div>

            <div class="actions">
                <button type="submit" class="btn">Cadastrar</button>
                <a href="<%= ctx %>/produto/listar" class="cancel-link">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>
