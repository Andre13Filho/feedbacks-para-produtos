package br.edu.faculdade.feedback.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton - garante apenas UMA conexão com o banco
public class ConnectionFactory {

    private static final String HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String PORT = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    private static final String NAME = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "feedback_db";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME + "?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String URL_SEM_BANCO = "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USUARIO = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String SENHA = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root";

    private static Connection instanciaConexao;

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        if (instanciaConexao == null || instanciaConexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado.", e);
            }
            instanciaConexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return instanciaConexao;
    }

    // Conexão sem banco selecionado - usada pelo DatabaseInitializer
    public static Connection getConnectionSemBanco() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado.", e);
        }
        return DriverManager.getConnection(URL_SEM_BANCO, USUARIO, SENHA);
    }
}
