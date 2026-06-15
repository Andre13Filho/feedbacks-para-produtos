package br.edu.faculdade.feedback.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton - garante apenas UMA conexão com o banco
public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/feedback_db?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String URL_SEM_BANCO = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

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
