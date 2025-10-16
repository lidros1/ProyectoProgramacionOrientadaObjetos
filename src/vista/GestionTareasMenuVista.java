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
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnCrearTarea = new JButton("Crear Tarea");
        btnListarTarea = new JButton("Listar Tareas");
        btnEditarTarea = new JButton("Editar Tarea");
        btnAsignarTarea = new JButton("Asignar Tarea");
        btnVolver = new JButton("Volver al Menú Principal");

        gbc.gridy = 0; add(btnCrearTarea, gbc);
        gbc.gridy = 1; add(btnListarTarea, gbc);
        gbc.gridy = 2; add(btnEditarTarea, gbc);
        gbc.gridy = 3; add(btnAsignarTarea, gbc);
        gbc.gridy = 4; add(new JSeparator(), gbc);
        gbc.gridy = 5; add(btnVolver, gbc);
    }

    // Getters para el controlador
    public JButton getBtnCrearTarea() { return btnCrearTarea; }
    public JButton getBtnListarTarea() { return btnListarTarea; }
    public JButton getBtnEditarTarea() { return btnEditarTarea; }
    public JButton getBtnAsignarTarea() { return btnAsignarTarea; }
    public JButton getBtnVolver() { return btnVolver; }
}