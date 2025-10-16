package controlador;

import modelo.Usuario;
import persistencia.PermisoDAO; // Importar PermisoDAO
import persistencia.UsuarioDAO;
import util.SesionUsuario;
import vista.DashboardKanbanVista;
import vista.LoginVista;
import vista.MenuPrincipalVista; // Importar MenuPrincipalVista

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Importar List

public class LoginControlador implements ActionListener {

    private final LoginVista loginVista;
    private final UsuarioDAO usuarioDAO;
    private final PermisoDAO permisoDAO; // Instancia de PermisoDAO

    public LoginControlador(LoginVista loginVista, UsuarioDAO usuarioDAO) {
        this.loginVista = loginVista;
        this.usuarioDAO = usuarioDAO;
        this.permisoDAO = new PermisoDAO(); // Inicializar PermisoDAO
        this.loginVista.getBotonIngresar().addActionListener(this);
    }

    public void iniciar() {
        loginVista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginVista.getBotonIngresar()) {
            manejarLogin();
        }
    }

    private void manejarLogin() {
        String nombreUsuario = loginVista.getCampoUsuario().getText();
        String contrasena = new String(loginVista.getCampoContrasena().getPassword());

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(loginVista, "El usuario y la contraseña no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = usuarioDAO.validarUsuario(nombreUsuario, contrasena);

        if (usuario != null) {
            // LOGIN EXITOSO
            loginVista.dispose(); // Cierra la ventana de login

            // 1. Actualizar la fecha de último acceso del usuario
            usuarioDAO.actualizarFechaUltimoAcceso(usuario.getIdUsuario());

            // 2. Cargar los permisos del usuario
            List<modelo.Permiso> permisos = permisoDAO.listarPermisosPorUsuario(usuario.getIdUsuario());
            usuario.setPermisos(permisos);

            // 3. Establecer el usuario logueado en la sesión
            SesionUsuario.setUsuarioLogueado(usuario);

            // 4. Mostrar el menú principal
            MenuPrincipalVista menuPrincipalVista = new MenuPrincipalVista();
            MenuPrincipalControlador menuPrincipalControlador = new MenuPrincipalControlador(menuPrincipalVista);
            menuPrincipalControlador.iniciar();

        } else {
            JOptionPane.showMessageDialog(loginVista, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}