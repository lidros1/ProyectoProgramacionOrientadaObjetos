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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnCrearProyecto = new JButton("Crear Proyecto");
        btnListarProyecto = new JButton("Listar Proyectos");
        btnEditarProyecto = new JButton("Editar Proyecto");
        btnDesignarProyecto = new JButton("Designar Proyecto");
        btnVolver = new JButton("Volver al Menú Principal");

        gbc.gridy = 0; add(btnCrearProyecto, gbc);
        gbc.gridy = 1; add(btnListarProyecto, gbc);
        gbc.gridy = 2; add(btnEditarProyecto, gbc);
        gbc.gridy = 3; add(btnDesignarProyecto, gbc);
        gbc.gridy = 4; add(new JSeparator(), gbc);
        gbc.gridy = 5; add(btnVolver, gbc);
    }

    // Getters para el controlador
    public JButton getBtnCrearProyecto() { return btnCrearProyecto; }
    public JButton getBtnListarProyecto() { return btnListarProyecto; }
    public JButton getBtnEditarProyecto() { return btnEditarProyecto; }
    public JButton getBtnDesignarProyecto() { return btnDesignarProyecto; }
    public JButton getBtnVolver() { return btnVolver; }
}