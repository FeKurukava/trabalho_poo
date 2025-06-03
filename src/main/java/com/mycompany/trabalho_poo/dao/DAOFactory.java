package com.mycompany.trabalho_poo.dao;

public class DAOFactory {
    
    private static DAOFactory instance;
    
    private final UsuarioDAO usuarioDAO;
    private final CategoriaDAO categoriaDAO;
    private final TransacaoDAO transacaoDAO;

    private DAOFactory() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.categoriaDAO = new CategoriaDAOImpl();
        this.transacaoDAO = new TransacaoDAOImpl();
    }

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public TransacaoDAO getTransacaoDAO() {
        return transacaoDAO;
    }

    public void closeResources() {
        GenericDAOImpl.closeEntityManagerFactory();
    }
}