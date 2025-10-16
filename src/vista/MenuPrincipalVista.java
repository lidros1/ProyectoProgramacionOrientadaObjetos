package vista;

import modelo.Usuario;
import util.SesionUsuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipalVista extends JFrame {
    private JButton btnGestionUsuarios;
    private JButton btnGestionProyectos;
    private JButton btnMisProyectos;
    private JButton btnGestionTareas;
    private JButton btnReportes;
    private JButton btnCerrarSesion;

    public MenuPrincipalVista() {
        setTitle("Menú Principal - Gestor de Proyectos");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnGestionUsuarios = new JButton("Gestión Usuarios");
        btnGestionProyectos = new JButton("Gestión Proyectos");
        btnMisProyectos = new JButton("Mis Proyectos");
        btnGestionTareas = new JButton("Gestión Tareas");
        btnReportes = new JButton("Reportes");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // --- ORDEN DE BOTONES MODIFICADO ---
        gbc.gridy = 0; add(btnMisProyectos, gbc);      // <-- MOVIDO AL PRIMER LUGAR
        gbc.gridy = 1; add(btnGestionUsuarios, gbc);
        gbc.gridy = 2; add(btnGestionProyectos, gbc);
        gbc.gridy = 3; add(btnGestionTareas, gbc);
        gbc.gridy = 4; add(btnReportes, gbc);
        gbc.gridy = 5; add(new JSeparator(), gbc);
        gbc.gridy = 6; add(btnCerrarSesion, gbc);

        configurarVisibilidadBotones();
    }

    private void configurarVisibilidadBotones() {
        Usuario usuarioLogueado = SesionUsuario.getUsuarioLogueado();
        if (usuarioLogueado != null) {
            btnGestionUsuarios.setVisible(usuarioLogueado.tienePermiso("Gestion de Usuarios", "Listar"));
            btnGestionProyectos.setVisible(usuarioLogueado.tienePermiso("Gestion de Proyectos", "Listar"));
            // Para "Mis Proyectos" asumimos que todos los usuarios logueados tienen acceso
            btnMisProyectos.setVisible(true);
            btnGestionTareas.setVisible(usuarioLogueado.tienePermiso("Gestion de Tareas", "Listar"));
            btnReportes.setVisible(usuarioLogueado.tienePermiso("Reportes", "Listar"));
        } else {
            btnGestionUsuarios.setVisible(false);
            btnGestionProyectos.setVisible(false);
            btnMisProyectos.setVisible(false);
            btnGestionTareas.setVisible(false);
            btnReportes.setVisible(false);
        }
    }

    // Getters
    public JButton getBtnGestionUsuarios() { return btnGestionUsuarios; }
    public JButton getBtnGestionProyectos() { return btnGestionProyectos; }
    public JButton getBtnMisProyectos() { return btnMisProyectos; }
    public JButton getBtnGestionTareas() { return btnGestionTareas; }
    public JButton getBtnReportes() { return btnReportes; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
}