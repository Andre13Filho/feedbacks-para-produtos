package br.edu.faculdade.feedback.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe utilitária para operações com JSON nos Controllers.
 * Centraliza a leitura do body da requisição e o envio de respostas JSON,
 * evitando repetição de código nos Servlets.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class JsonUtil {

    // Instância do Gson com formatação bonita (indentada) para facilitar a leitura
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private JsonUtil() {}

    /**
     * Retorna a instância do Gson configurada.
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * Lê o corpo (body) da requisição HTTP e converte para o tipo desejado.
     * Usado quando o cliente envia dados em JSON no body (ex: POST via Postman).
     *
     * @param req   requisição HTTP
     * @param classe tipo da classe para converter o JSON
     * @return objeto do tipo informado, preenchido com os dados do JSON
     */
    public static <T> T lerBody(HttpServletRequest req, Class<T> classe) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha);
            }
        }
        return gson.fromJson(sb.toString(), classe);
    }

    /**
     * Envia uma resposta JSON com o status HTTP informado.
     * Configura os headers corretos (Content-Type e charset).
     *
     * @param resp   resposta HTTP
     * @param status código de status (ex: 200, 201, 400, 404)
     * @param dados  objeto que será convertido para JSON
     */
    public static void enviarResposta(HttpServletResponse resp, int status, Object dados) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);

        try (PrintWriter writer = resp.getWriter()) {
            writer.print(gson.toJson(dados));
            writer.flush();
        }
    }

    /**
     * Envia uma resposta JSON de erro com uma mensagem.
     * Formato: { "erro": "mensagem aqui" }
     */
    public static void enviarErro(HttpServletResponse resp, int status, String mensagem) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("erro", mensagem);
        enviarResposta(resp, status, json);
    }

    /**
     * Envia uma resposta JSON de sucesso com uma mensagem.
     * Formato: { "mensagem": "mensagem aqui" }
     */
    public static void enviarSucesso(HttpServletResponse resp, int status, String mensagem) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("mensagem", mensagem);
        enviarResposta(resp, status, json);
    }
}
