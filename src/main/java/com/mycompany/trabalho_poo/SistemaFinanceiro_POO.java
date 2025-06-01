
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

    private static final DAOFactory daoFactory = DAOFactory.getInstance();

    public static Usuario usuarioLogado = null;

    public static List<Usuario> getUsuariosCadastrados() {
        return daoFactory.getUsuarioDAO().findAll();
    }

    public static Usuario saveUsuario(Usuario usuario) {
        return daoFactory.getUsuarioDAO().save(usuario);
    }

    public static Usuario updateUsuario(Usuario usuario) {
        return daoFactory.getUsuarioDAO().update(usuario);
    }

    public static Transacao saveTransacao(Transacao transacao, Usuario usuario) {
        transacao.setUsuario(usuario);
        Transacao savedTransacao = daoFactory.getTransacaoDAO().save(transacao);
        usuario.getTransacao().add(savedTransacao);
        updateUsuario(usuario);
        return savedTransacao;
    }

    public static Categoria saveCategoria(Categoria categoria, Usuario usuario) {
        Categoria savedCategoria = daoFactory.getCategoriaDAO().save(categoria);
        usuario.getCategoria().add(savedCategoria);
        updateUsuario(usuario);
        return savedCategoria;
    }

    public static Categoria updateCategoria(Categoria categoria) {
        return daoFactory.getCategoriaDAO().update(categoria);
    }

    public static void deleteCategoria(Categoria categoria) {
        try {
            if (categoria.getId() != null) {
                Categoria existingCategoria = daoFactory.getCategoriaDAO().findById(categoria.getId());
                if (existingCategoria != null) {
                    daoFactory.getCategoriaDAO().delete(existingCategoria);
                } else {
                    System.err.println("Categoria com ID " + categoria.getId() + " não encontrada no banco de dados.");
                }
            } else {
                System.err.println("Tentativa de excluir categoria sem ID.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir categoria: " + e.getMessage());
            throw e;
        }
    }

    public static void deleteTransacao(Transacao transacao) {
        daoFactory.getTransacaoDAO().delete(transacao);
    }

    public static Usuario findUsuarioByLogin(String login) {
        return daoFactory.getUsuarioDAO().findByLogin(login);
    }

    public static Usuario authenticateUsuario(String login, String senha) {
        return daoFactory.getUsuarioDAO().authenticate(login, senha);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                daoFactory.closeResources();
            }));

            abrirNovoFrame("Login", new LoginPanel());
        });
    }
}
