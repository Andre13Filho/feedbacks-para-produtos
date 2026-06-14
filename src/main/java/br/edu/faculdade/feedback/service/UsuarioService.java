package br.edu.faculdade.feedback.service;

import br.edu.faculdade.feedback.model.dao.UsuarioDAO;
import br.edu.faculdade.feedback.model.entity.Usuario;

import java.sql.SQLException;

/**
 * Camada de serviço do Usuario.
 * Valida as regras de negócio antes de acessar o banco.
 *
 * Projeto: Aplicativo de Feedback para Produtos
 * Autores: André (5169692) e Otávio (5167958)
 */
public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Realiza o login do usuário validando e-mail e senha.
     * Verifica se os campos foram preenchidos e se as credenciais
     * correspondem a um registro no banco de dados.
     *
     * @param email e-mail informado
     * @param senha senha informada
     * @return objeto Usuario se o login for válido
     * @throws IllegalArgumentException se os campos estiverem vazios ou credenciais inválidas
     * @throws SQLException se ocorrer erro no banco
     */
    public Usuario login(String email, String senha) throws IllegalArgumentException, SQLException {
        // Validação: e-mail obrigatório
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        // Validação: senha obrigatória
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        // Busca no banco pelo e-mail e senha
        Usuario usuario = usuarioDAO.buscarPorEmailESenha(email.trim(), senha);

        // Se retornou null, as credenciais estão incorretas
        if (usuario == null) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        return usuario;
    }

    /**
     * Cadastra um novo usuário no sistema.
     */
    public Usuario cadastrar(String nome, String email, String senha)
            throws IllegalArgumentException, SQLException {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        Usuario usuario = new Usuario(nome.trim(), email.trim(), senha);
        usuarioDAO.criar(usuario);
        return usuario;
    }

    /**
     * Busca um usuário pelo ID.
     */
    public Usuario buscarPorId(int id) throws SQLException {
        return usuarioDAO.buscarPorId(id);
    }
}
