package principal;

import controlador.LoginControlador;
import persistencia.UsuarioDAO;
import vista.LoginVista;
import javax.swing.*;

public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Iniciar la aplicación desde la vista de login
            LoginVista vistaLogin = new LoginVista();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            LoginControlador controladorLogin = new LoginControlador(vistaLogin, usuarioDAO);
            controladorLogin.iniciar();
        });
    }
}