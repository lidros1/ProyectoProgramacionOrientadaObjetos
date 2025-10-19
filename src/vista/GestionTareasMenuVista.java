package vista;

import javax.swing.*;
import java.awt.*;

public class GestionTareasMenuVista extends JFrame {
    private JButton btnCrearTarea;
    private JButton btnListarTarea;
    private JButton btnAsignarTarea;
    private JButton btnVolver;

    public GestionTareasMenuVista() {
        setTitle("Gestión de Tareas");
        setSize(400, 350); // Reducimos un poco la altura
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnCrearTarea = new JButton("Crear Tarea");
        btnListarTarea = new JButton("Listar Tareas");
        btnAsignarTarea = new JButton("Asignar Tarea a Usuarios"); // Nombre más descriptivo
        btnVolver = new JButton("Volver al Menú Principal");

        gbc.gridy = 0; add(btnCrearTarea, gbc);
        gbc.gridy = 1; add(btnListarTarea, gbc);
        gbc.gridy = 2; add(btnAsignarTarea, gbc);
        gbc.gridy = 3; add(new JSeparator(), gbc);
        gbc.gridy = 4; add(btnVolver, gbc);
    }

    // Getters
    public JButton getBtnCrearTarea() { return btnCrearTarea; }
    public JButton getBtnListarTarea() { return btnListarTarea; }
    public JButton getBtnAsignarTarea() { return btnAsignarTarea; }
    public JButton getBtnVolver() { return btnVolver; }
}