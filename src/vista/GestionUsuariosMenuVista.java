package vista;

import javax.swing.*;
import java.awt.*;

public class GestionUsuariosMenuVista extends JFrame {
    private JButton btnCrearUsuario;
    private JButton btnListarUsuario;
    private JButton btnEditarUsuario;
    private JButton btnVolver;

    public GestionUsuariosMenuVista() {
        setTitle("Gestión de Usuarios");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnCrearUsuario = new JButton("Crear Usuario");
        btnListarUsuario = new JButton("Listar Usuario");
        btnEditarUsuario = new JButton("Editar Usuario");
        btnVolver = new JButton("Volver al Menú Principal");

        gbc.gridy = 0; add(btnCrearUsuario, gbc);
        gbc.gridy = 1; add(btnListarUsuario, gbc);
        gbc.gridy = 2; add(btnEditarUsuario, gbc);
        gbc.gridy = 3; add(new JSeparator(), gbc); // Un separador visual
        gbc.gridy = 4; add(btnVolver, gbc);
    }

    // Getters para el controlador
    public JButton getBtnCrearUsuario() { return btnCrearUsuario; }
    public JButton getBtnListarUsuario() { return btnListarUsuario; }
    public JButton getBtnEditarUsuario() { return btnEditarUsuario; }
    public JButton getBtnVolver() { return btnVolver; }
}