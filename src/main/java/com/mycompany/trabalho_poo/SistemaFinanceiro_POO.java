/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.trabalho_poo;

import com.mycompany.trabalho_poo.dao.DAOFactory;
import com.mycompany.trabalho_poo.dao.UsuarioDAO;
import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import com.mycompany.trabalho_poo.panels.login.LoginPanel;

import java.util.List;
import javax.swing.SwingUtilities;

import static com.mycompany.trabalho_poo.utils.SwingUtils.abrirNovoFrame;


public class SistemaFinanceiro_POO {

    // DAO factory instance
    private static final DAOFactory daoFactory = DAOFactory.getInstance();

    // Static reference to the currently logged in user
    public static Usuario usuarioLogado = null;

    /**
     * Gets all registered users from the database.
     * 
     * @return A list of all users
     */
    public static List<Usuario> getUsuariosCadastrados() {
        return daoFactory.getUsuarioDAO().findAll();
    }

    /**
     * Saves a new user to the database.
     * 
     * @param usuario The user to save
     * @return The saved user
     */
    public static Usuario saveUsuario(Usuario usuario) {
        return daoFactory.getUsuarioDAO().save(usuario);
    }

    /**
     * Updates an existing user in the database.
     * 
     * @param usuario The user to update
     * @return The updated user
     */
    public static Usuario updateUsuario(Usuario usuario) {
        return daoFactory.getUsuarioDAO().update(usuario);
    }

    /**
     * Saves a new transaction to the database and associates it with a user.
     * 
     * @param transacao The transaction to save
     * @param usuario The user to associate with the transaction
     * @return The saved transaction
     */
    public static Transacao saveTransacao(Transacao transacao, Usuario usuario) {
        transacao.setUsuario(usuario);
        Transacao savedTransacao = daoFactory.getTransacaoDAO().save(transacao);
        usuario.getTransacao().add(savedTransacao);
        updateUsuario(usuario);
        return savedTransacao;
    }

    /**
     * Saves a new category to the database and associates it with a user.
     * 
     * @param categoria The category to save
     * @param usuario The user to associate with the category
     * @return The saved category
     */
    public static Categoria saveCategoria(Categoria categoria, Usuario usuario) {
        Categoria savedCategoria = daoFactory.getCategoriaDAO().save(categoria);
        usuario.getCategoria().add(savedCategoria);
        updateUsuario(usuario);
        return savedCategoria;
    }

    /**
     * Updates a category in the database.
     * 
     * @param categoria The category to update
     * @return The updated category
     */
    public static Categoria updateCategoria(Categoria categoria) {
        return daoFactory.getCategoriaDAO().update(categoria);
    }

    /**
     * Deletes a category from the database.
     * 
     * @param categoria The category to delete
     */
    public static void deleteCategoria(Categoria categoria) {
        daoFactory.getCategoriaDAO().delete(categoria);
    }

    /**
     * Deletes a transaction from the database.
     * 
     * @param transacao The transaction to delete
     */
    public static void deleteTransacao(Transacao transacao) {
        daoFactory.getTransacaoDAO().delete(transacao);
    }

    /**
     * Finds a user by login.
     * 
     * @param login The login to search for
     * @return The found user, or null if not found
     */
    public static Usuario findUsuarioByLogin(String login) {
        return daoFactory.getUsuarioDAO().findByLogin(login);
    }

    /**
     * Authenticates a user with login and password.
     * 
     * @param login The user's login
     * @param senha The user's password
     * @return The authenticated user, or null if authentication fails
     */
    public static Usuario authenticateUsuario(String login, String senha) {
        return daoFactory.getUsuarioDAO().authenticate(login, senha);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Add shutdown hook to close resources when the application exits
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                daoFactory.closeResources();
            }));

            abrirNovoFrame("Login", new LoginPanel());
        });
    }
}
