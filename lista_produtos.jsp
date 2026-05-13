<%@ page import="java.util.List" %>
    <%@ page import="br.com.mvc.model.Produto" %>
        <% List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
                String ctx = request.getContextPath();
                %>
                <!DOCTYPE html>
                <html lang="pt-BR">

                <head>
                    <meta charset="UTF-8">
                    <title>Produtos para Feedback</title>
                    <style>
                        /* Utilize o CSS do seu material aqui (classes .container, .btn, .btn-success)  */
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f5f5f5;
                            padding: 24px;
                        }

                        .container {
                            max-width: 800px;
                            margin: 0 auto;
                            background: #fff;
                            padding: 24px;
                            border-radius: 8px;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        th {
                            background: #1976d2;
                            color: #fff;
                            padding: 10px;
                            text-align: left;
                        }

                        td {
                            border: 1px solid #ddd;
                            padding: 10px;
                        }

                        .btn {
                            padding: 8px 16px;
                            background: #1976d2;
                            color: #fff;
                            text-decoration: none;
                            border-radius: 4px;
                        }
                    </style>
                </head>

                <body>
                    <div class="container">
                        <h1>Produtos Disponíveis</h1>
                        <table>
                            <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>Preço</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Produto p : produtos) { %>
                                    <tr>
                                        <td>
                                            <%= p.getNome() %>
                                        </td>
                                        <td>R$ <%= p.getPreco() %>
                                        </td>
                                        <td>
                                            <a class="btn"
                                                href="<%= ctx %>/produto/avaliar?id=<%= p.getId() %>">Avaliar</a>
                                        </td>
                                    </tr>
                                    <% } %>
                            </tbody>
                        </table>
                    </div>
                </body>

                </html>