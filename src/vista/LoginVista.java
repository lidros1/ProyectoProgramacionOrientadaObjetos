package vista;

import javax.swing.*;
import java.awt.*;

public class LoginVista extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;

    public LoginVista() {
        setTitle("Inicio de Sesión - Gestor de Proyectos");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        campoUsuario = new JTextField(15);
        add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        campoContrasena = new JPasswordField(15);
        add(campoContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        botonIngresar = new JButton("Ingresar");
        add(botonIngresar, gbc);
    }

    // Getters para el controlador
    public JTextField getCampoUsuario() { return campoUsuario; }
    public JPasswordField getCampoContrasena() { return campoContrasena; }
    public JButton getBotonIngresar() { return botonIngresar; }
}