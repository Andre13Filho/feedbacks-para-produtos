<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.edu.faculdade.feedback.model.entity.Produto" %>
<% 
    List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
    String ctx = request.getContextPath();
    String sucesso = request.getParameter("sucesso");
    String erro = request.getParameter("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produtos para Feedback</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            padding: 24px;
            margin: 0;
            color: #333;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
            background: #fff;
            padding: 32px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        h1 {
            margin-top: 0;
            color: #1a73e8;
            font-size: 28px;
            margin-bottom: 24px;
            border-bottom: 2px solid #e8eaed;
            padding-bottom: 12px;
        }

        .alert-success {
            background-color: #e6f4ea;
            color: #137333;
            padding: 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            border: 1px solid #ceead6;
            font-weight: 500;
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

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 8px;
        }

        th {
            background: #f8f9fa;
            color: #5f6368;
            padding: 14px 16px;
            text-align: left;
            font-weight: 600;
            border-bottom: 2px solid #dadce0;
        }

        td {
            border-bottom: 1px solid #f1f3f4;
            padding: 16px;
            vertical-align: middle;
            color: #3c4043;
        }

        tr:hover {
            background-color: #f8f9fa;
        }

        .btn {
            display: inline-block;
            padding: 8px 20px;
            background: #1a73e8;
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 500;
            transition: background 0.2s;
        }

        .btn:hover {
            background: #1557b0;
        }

        .empty-message {
            text-align: center;
            padding: 32px;
            color: #80868b;
            font-style: italic;
        }
    </style>
</head>

<body>
    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #e8eaed; margin-bottom: 24px; padding-bottom: 12px;">
            <h1 style="margin: 0; border: none; padding: 0;">Produtos Disponíveis</h1>
            <a class="btn" href="<%= ctx %>/produto/novo" style="background: #34a853;">+ Novo Produto</a>
        </div>
        
        <% if ("true".equals(sucesso)) { %>
            <div class="alert-success">
                ✔ Feedback enviado com sucesso! Muito obrigado pela sua avaliação.
            </div>
        <% } %>

        <% if (request.getParameter("sucesso_produto") != null) { %>
            <div class="alert-success">
                ✔ Produto cadastrado com sucesso!
            </div>
        <% } %>

        <% if (erro != null && !erro.isEmpty()) { %>
            <div class="alert-danger">
                ⚠ Erro: <%= erro %>
            </div>
        <% } %>

        <table>
            <thead>
                <tr>
                    <th style="width: 30%;">Nome</th>
                    <th style="width: 50%;">Descrição</th>
                    <th style="width: 20%; text-align: center;">Ações</th>
                </tr>
            </thead>
            <tbody>
                <% if (produtos != null && !produtos.isEmpty()) { %>
                    <% for (Produto p : produtos) { %>
                        <tr>
                            <td style="font-weight: 600;">
                                <%= p.getNome() %>
                            </td>
                            <td>
                                <%= p.getDescricao() != null ? p.getDescricao() : "Sem descrição" %>
                            </td>
                            <td style="text-align: center;">
                                <a class="btn" href="<%= ctx %>/produto/detalhes?id=<%= p.getId() %>" style="background: #fbbc04; color: #000; margin-right: 8px;">★ Avaliações</a>
                                <a class="btn" href="<%= ctx %>/produto/avaliar?id=<%= p.getId() %>">Deixar Feedback</a>
                            </td>
                        </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="3" class="empty-message">Nenhum produto cadastrado ou disponível no momento.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
