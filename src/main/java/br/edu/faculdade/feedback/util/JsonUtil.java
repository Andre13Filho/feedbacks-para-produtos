package br.edu.faculdade.feedback.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtil() {}

    public static Gson getGson() {
        return gson;
    }

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

    public static void enviarResposta(HttpServletResponse resp, int status, Object dados) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        try (PrintWriter writer = resp.getWriter()) {
            writer.print(gson.toJson(dados));
            writer.flush();
        }
    }

    public static void enviarErro(HttpServletResponse resp, int status, String mensagem) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("erro", mensagem);
        enviarResposta(resp, status, json);
    }

    public static void enviarSucesso(HttpServletResponse resp, int status, String mensagem) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("mensagem", mensagem);
        enviarResposta(resp, status, json);
    }
}
