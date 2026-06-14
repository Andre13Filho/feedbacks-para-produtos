package br.edu.faculdade.feedback.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária que gerencia a conexão JDBC com o MySQL
 * utilizando o padrão de projeto Singleton. Isso garante que
 * exista apenas UMA instância da conexão ativa por vez,
 * evitando desperdício de recursos.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class ConnectionFactory {

    // Configurações de conexão com o banco local (sem Docker)
    private static final String URL    = "jdbc:mysql://localhost:3306/feedback_db?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA   = "root"; // altere conforme seu ambiente

    // Atributo estático que guarda a ÚNICA instância da conexão (Singleton)
    private static Connection instanciaConexao;

    // Construtor privado: impede que criem objetos desta classe
    private ConnectionFactory() {}

    /**
     * Retorna a conexão Singleton com o banco de dados.
     * Se a conexão ainda não existe ou foi fechada, cria uma nova.
     * Caso contrário, reutiliza a conexão existente.
     *
     * @return Connection conexão ativa com o MySQL
     * @throws SQLException se não conseguir conectar
     */
    public static Connection getConnection() throws SQLException {
        // Verifica se a conexão não existe ou se foi fechada
        if (instanciaConexao == null || instanciaConexao.isClosed()) {
            try {
                // Carrega o driver do MySQL na memória
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado no classpath.", e);
            }
            // Cria a conexão e guarda no atributo estático
            instanciaConexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return instanciaConexao;
    }
}
