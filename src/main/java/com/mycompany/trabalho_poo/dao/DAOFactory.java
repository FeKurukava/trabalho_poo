package com.mycompany.trabalho_poo.dao;

/**
 * Factory class for creating DAO instances.
 * This class follows the Singleton pattern to ensure only one instance exists.
 */
public class DAOFactory {
    
    private static DAOFactory instance;
    
    private final UsuarioDAO usuarioDAO;
    private final CategoriaDAO categoriaDAO;
    private final TransacaoDAO transacaoDAO;
    
    /**
     * Private constructor to prevent direct instantiation.
     */
    private DAOFactory() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.categoriaDAO = new CategoriaDAOImpl();
        this.transacaoDAO = new TransacaoDAOImpl();
    }
    
    /**
     * Gets the singleton instance of the factory.
     * 
     * @return The singleton instance
     */
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    /**
     * Gets the UsuarioDAO instance.
     * 
     * @return The UsuarioDAO instance
     */
    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }
    
    /**
     * Gets the CategoriaDAO instance.
     * 
     * @return The CategoriaDAO instance
     */
    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }
    
    /**
     * Gets the TransacaoDAO instance.
     * 
     * @return The TransacaoDAO instance
     */
    public TransacaoDAO getTransacaoDAO() {
        return transacaoDAO;
    }
    
    /**
     * Closes all resources used by the DAOs.
     * Should be called when the application is shutting down.
     */
    public void closeResources() {
        GenericDAOImpl.closeEntityManagerFactory();
    }
}