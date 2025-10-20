// Archivo: src/vista/MenuPrincipalVista.java
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Aplicar propiedades de la ventana (maximizar, etc.)
        TemaPersonalizado.configurarVentana(this);

        // Panel principal con GridBagLayout para centrar los botones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // --- AJUSTE CLAVE: Ancho preferido de los botones ---
        // Hacemos que los botones tengan un ancho preferido para que no se expandan infinitamente.
        Dimension buttonSize = new Dimension(350, 40);

        btnGestionUsuarios = new JButton("Gestión Usuarios");
        btnGestionProyectos = new JButton("Gestión Proyectos");
        btnMisProyectos = new JButton("Mis Proyectos");
        btnGestionTareas = new JButton("Gestión Tareas");
        btnReportes = new JButton("Reportes");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // Asignar tamaño preferido a cada botón
        btnMisProyectos.setPreferredSize(buttonSize);
        btnGestionUsuarios.setPreferredSize(buttonSize);
        btnGestionProyectos.setPreferredSize(buttonSize);
        btnGestionTareas.setPreferredSize(buttonSize);
        btnReportes.setPreferredSize(buttonSize);
        btnCerrarSesion.setPreferredSize(buttonSize);

        // Aplicar estilos a los botones
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnMisProyectos);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGestionUsuarios);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGestionProyectos);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGestionTareas);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnReportes);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnCerrarSesion);

        gbc.gridy = 0; panelCentral.add(btnMisProyectos, gbc);
        gbc.gridy = 1; panelCentral.add(btnGestionUsuarios, gbc);
        gbc.gridy = 2; panelCentral.add(btnGestionProyectos, gbc);
        gbc.gridy = 3; panelCentral.add(btnGestionTareas, gbc);
        gbc.gridy = 4; panelCentral.add(btnReportes, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridy = 5; panelCentral.add(btnCerrarSesion, gbc);

        add(panelCentral, BorderLayout.CENTER);

        configurarVisibilidadBotones();
    }

    private void configurarVisibilidadBotones() {
        Usuario usuarioLogueado = SesionUsuario.getUsuarioLogueado();
        if (usuarioLogueado != null) {
            btnGestionUsuarios.setVisible(usuarioLogueado.tienePermiso("Gestion de Usuarios", "Listar"));
            btnGestionProyectos.setVisible(usuarioLogueado.tienePermiso("Gestion de Proyectos", "Listar"));
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