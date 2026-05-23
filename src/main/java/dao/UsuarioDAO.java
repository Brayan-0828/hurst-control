package dao;

import model.Usuario;

public class UsuarioDAO extends GenericDao<Usuario> {
    public UsuarioDAO() {
        super(Usuario.class);
    }
}
