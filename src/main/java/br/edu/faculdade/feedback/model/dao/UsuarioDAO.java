package br.edu.faculdade.feedback.model.dao;

import br.edu.faculdade.feedback.model.entity.Usuario;
import br.edu.faculdade.feedback.util.ConnectionFactory;

import java.sql.*;

/**
 * DAO da entidade Usuario.
 * Responsável pelas operações SQL na tabela Usuarios.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class UsuarioDAO {

    /**
     * Insere um novo usuário no banco de dados.
     */
    public void criar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (nome, email, senha) VALUES (?, ?, ?)";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();

            try (ResultSet chaveGerada = stmt.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    usuario.setId(chaveGerada.getInt(1));
                }
            }
        }
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @return Usuario encontrado ou null se não existir
     */
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM Usuarios WHERE id = ?";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca um usuário pelo e-mail e senha.
     * Utilizado na autenticação (login) do sistema.
     *
     * @param email e-mail informado
     * @param senha senha informada
     * @return Usuario se as credenciais estiverem corretas, ou null
     */
    public Usuario buscarPorEmailESenha(String email, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM Usuarios WHERE email = ? AND senha = ?";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        return null;
    }

    /**
     * Converte uma linha do ResultSet em objeto Usuario.
     */
    private Usuario mapearResultado(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("senha")
        );
    }
}
