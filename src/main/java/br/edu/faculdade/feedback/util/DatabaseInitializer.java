package br.edu.faculdade.feedback.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Cria o banco, tabelas e dados de exemplo ao iniciar o Tomcat
@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            criarBanco();
            criarTabelas();
            inserirDadosDeExemplo();
            System.out.println("[DB] Banco de dados pronto!");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao inicializar: " + e.getMessage());
        }
    }

    private void criarBanco() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnectionSemBanco();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS feedback_db " +
                "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
            );
        }
    }

    private void criarTabelas() throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Usuarios (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "nome VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) NOT NULL UNIQUE, " +
                "senha VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (id))"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Produtos (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "nome VARCHAR(100) NOT NULL, " +
                "descricao TEXT, " +
                "PRIMARY KEY (id))"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Feedback (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "usuario_id INT NOT NULL, " +
                "produto_id INT NOT NULL, " +
                "nota TINYINT NOT NULL, " +
                "comentario TEXT, " +
                "data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (id), " +
                "CONSTRAINT fk_feedback_usuario FOREIGN KEY (usuario_id) REFERENCES Usuarios (id), " +
                "CONSTRAINT fk_feedback_produto FOREIGN KEY (produto_id) REFERENCES Produtos (id), " +
                "CONSTRAINT chk_nota CHECK (nota BETWEEN 1 AND 5))"
            );
        }
    }

    private void inserirDadosDeExemplo() throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        try (Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM Usuarios");
            rs.next();
            if (rs.getInt(1) > 0) return;

            stmt.executeUpdate(
                "INSERT INTO Usuarios (nome, email, senha) VALUES " +
                "('André Silva', 'andre@email.com', 'senha123'), " +
                "('Otávio Santos', 'otavio@email.com', 'senha456')"
            );
            stmt.executeUpdate(
                "INSERT INTO Produtos (nome, descricao) VALUES " +
                "('Notebook Gamer', 'Notebook de alto desempenho para jogos.'), " +
                "('Mouse Sem Fio', 'Mouse ergonômico com bateria de longa duração.'), " +
                "('Teclado Mecânico', 'Teclado com switches azuis e retroiluminação RGB.')"
            );
            stmt.executeUpdate(
                "INSERT INTO Feedback (usuario_id, produto_id, nota, comentario) VALUES " +
                "(1, 1, 5, 'Excelente produto, superou minhas expectativas!'), " +
                "(2, 1, 4, 'Muito bom, mas poderia ser mais leve.'), " +
                "(1, 2, 3, 'Mouse bom, porém a bateria dura menos do que o anunciado.')"
            );
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
