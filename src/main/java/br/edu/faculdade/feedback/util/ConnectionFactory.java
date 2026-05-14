package br.edu.faculdade.feedback.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {

    // --- Configurações de conexão ---

    private static final String URL      = "jdbc:mysql://localhost:3306/feedback_db?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO  = "root";
    private static final String SENHA    = "root";


    private ConnectionFactory() {}


    public static Connection getConnection() throws SQLException {
        // DriverManager usa a URL para identificar qual driver carregar
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
