// Archivo: src/vista/GestionProyectosMenuVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class GestionProyectosMenuVista extends JFrame {
    private JButton btnCrearProyecto;
    private JButton btnListarProyecto;
    private JButton btnEditarProyecto;
    private JButton btnDesignarProyecto;
    private JButton btnVolver;

    public GestionProyectosMenuVista() {
        setTitle("Gestión de Proyectos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Corrección de tamaño y estilo de fondo
        TemaPersonalizado.configurarVentana(this);

        // Panel central para los botones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.ipadx = 100;

        btnCrearProyecto = new JButton("Crear Proyecto");
        btnListarProyecto = new JButton("Listar Proyectos");
        btnEditarProyecto = new JButton("Editar Proyecto");
        btnDesignarProyecto = new JButton("Designar Equipo a Proyecto");
        btnVolver = new JButton("Volver al Menú Principal");

        // Aplicar estilos
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrearProyecto);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnListarProyecto);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnEditarProyecto);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnDesignarProyecto);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        gbc.gridy = 0; panelCentral.add(btnCrearProyecto, gbc);
        gbc.gridy = 1; panelCentral.add(btnListarProyecto, gbc);
        gbc.gridy = 2; panelCentral.add(btnEditarProyecto, gbc);
        gbc.gridy = 3; panelCentral.add(btnDesignarProyecto, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridy = 4; panelCentral.add(btnVolver, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters para el controlador
    public JButton getBtnCrearProyecto() { return btnCrearProyecto; }
    public JButton getBtnListarProyecto() { return btnListarProyecto; }
    public JButton getBtnEditarProyecto() { return btnEditarProyecto; }
    public JButton getBtnDesignarProyecto() { return btnDesignarProyecto; }
    public JButton getBtnVolver() { return btnVolver; }
}