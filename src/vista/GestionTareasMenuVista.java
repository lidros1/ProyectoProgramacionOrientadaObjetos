// Archivo: src/vista/GestionTareasMenuVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class GestionTareasMenuVista extends JFrame {
    private JButton btnCrearTarea;
    private JButton btnListarTarea;
    private JButton btnEditarTarea;
    private JButton btnAsignarTarea;
    private JButton btnVolver;

    public GestionTareasMenuVista() {
        setTitle("Gestión de Tareas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        Dimension buttonSize = new Dimension(350, 40);

        btnCrearTarea = new JButton("Crear Tarea");
        btnListarTarea = new JButton("Listar Tareas");
        btnEditarTarea = new JButton("Editar Tarea");
        btnAsignarTarea = new JButton("Asignar Tarea a Usuarios");
        btnVolver = new JButton("Volver al Menú Principal");

        btnCrearTarea.setPreferredSize(buttonSize);
        btnListarTarea.setPreferredSize(buttonSize);
        btnEditarTarea.setPreferredSize(buttonSize);
        btnAsignarTarea.setPreferredSize(buttonSize);
        btnVolver.setPreferredSize(buttonSize);

        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrearTarea);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnListarTarea);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnEditarTarea);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnAsignarTarea);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        gbc.gridy = 0; panelCentral.add(btnCrearTarea, gbc);
        gbc.gridy = 1; panelCentral.add(btnListarTarea, gbc);
        gbc.gridy = 2; panelCentral.add(btnEditarTarea, gbc);
        gbc.gridy = 3; panelCentral.add(btnAsignarTarea, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridy = 4; panelCentral.add(btnVolver, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters
    public JButton getBtnCrearTarea() { return btnCrearTarea; }
    public JButton getBtnListarTarea() { return btnListarTarea; }
    public JButton getBtnEditarTarea() { return btnEditarTarea; }
    public JButton getBtnAsignarTarea() { return btnAsignarTarea; }
    public JButton getBtnVolver() { return btnVolver; }
}