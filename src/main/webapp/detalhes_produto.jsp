<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.edu.faculdade.feedback.model.entity.Produto" %>
<%@ page import="br.edu.faculdade.feedback.model.entity.Feedback" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% 
    String ctx = request.getContextPath();
    Produto produto = (Produto) request.getAttribute("produto");
    List<Feedback> feedbacks = (List<Feedback>) request.getAttribute("feedbacks");
    Double media = (Double) request.getAttribute("media");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Detalhes do Produto</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            padding: 24px;
            margin: 0;
            color: #333;
        }

        .container {
            max-width: 800px;
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
            margin-bottom: 8px;
        }

        .desc {
            color: #5f6368;
            margin-bottom: 24px;
            font-size: 16px;
            line-height: 1.5;
        }

        .stats {
            display: flex;
            align-items: center;
            background: #e8f0fe;
            padding: 16px 24px;
            border-radius: 8px;
            margin-bottom: 32px;
        }

        .stats-item {
            margin-right: 48px;
        }

        .stats-value {
            font-size: 24px;
            font-weight: 700;
            color: #1a73e8;
        }

        .stats-label {
            font-size: 14px;
            color: #5f6368;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-top: 4px;
        }

        .section-title {
            font-size: 20px;
            color: #202124;
            border-bottom: 2px solid #e8eaed;
            padding-bottom: 12px;
            margin-bottom: 24px;
        }

        .feedback-card {
            border: 1px solid #dadce0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 16px;
        }

        .feedback-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
        }

        .feedback-rating {
            color: #fbbc04;
            font-size: 18px;
            font-weight: bold;
        }

        .feedback-date {
            color: #80868b;
            font-size: 13px;
        }

        .feedback-comment {
            color: #3c4043;
            line-height: 1.5;
        }

        .actions {
            margin-top: 32px;
        }

        .btn {
            display: inline-block;
            padding: 10px 24px;
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

        .btn-outline {
            background: transparent;
            color: #1a73e8;
            border: 1px solid #1a73e8;
            margin-left: 12px;
        }

        .btn-outline:hover {
            background: #e8f0fe;
        }

        .empty-message {
            color: #80868b;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <% if (produto != null) { %>
            <h1><%= produto.getNome() %></h1>
            <div class="desc"><%= produto.getDescricao() != null ? produto.getDescricao() : "Sem descrição detalhada." %></div>
            
            <div class="stats">
                <div class="stats-item">
                    <div class="stats-value"><%= String.format("%.1f", media) %> / 5.0</div>
                    <div class="stats-label">Média de Avaliações</div>
                </div>
                <div class="stats-item">
                    <div class="stats-value"><%= feedbacks != null ? feedbacks.size() : 0 %></div>
                    <div class="stats-label">Total de Feedbacks</div>
                </div>
            </div>

            <h2 class="section-title">Comentários dos Usuários</h2>

            <% if (feedbacks != null && !feedbacks.isEmpty()) { %>
                <% for (Feedback f : feedbacks) { %>
                    <div class="feedback-card">
                        <div class="feedback-header">
                            <div class="feedback-rating">
                                <% for(int i=0; i<f.getNota(); i++) { %>★<% } %><% for(int i=f.getNota(); i<5; i++) { %>☆<% } %>
                            </div>
                            <div class="feedback-date">
                                <%= f.getDataCriacao() != null ? f.getDataCriacao().format(formatter) : "" %>
                            </div>
                        </div>
                        <div class="feedback-comment">
                            "<%= f.getComentario() %>"
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <p class="empty-message">Este produto ainda não recebeu nenhuma avaliação. Seja o primeiro!</p>
            <% } %>

            <div class="actions">
                <a class="btn" href="<%= ctx %>/produto/avaliar?id=<%= produto.getId() %>">Deixar Feedback</a>
                <a class="btn btn-outline" href="<%= ctx %>/produto/listar">Voltar para Lista</a>
            </div>
            
        <% } else { %>
            <h1>Produto não encontrado</h1>
            <div class="actions">
                <a class="btn" href="<%= ctx %>/produto/listar">Voltar para Lista</a>
            </div>
        <% } %>
    </div>
</body>
</html>
