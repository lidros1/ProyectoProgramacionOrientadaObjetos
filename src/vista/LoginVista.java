// Archivo: src/vista/LoginVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class LoginVista extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;

    public LoginVista() {
        setTitle("Inicio de Sesión - Gestor de Proyectos");
        setSize(450, 300); // Un tamaño fijo y más grande para el login
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(ConstantesUI.FUENTE_TITULO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(ConstantesUI.FUENTE_NORMAL);
        panelCentral.add(lblUsuario, gbc);

        gbc.gridx = 1;
        campoUsuario = new JTextField(15);
        campoUsuario.setFont(ConstantesUI.FUENTE_NORMAL);
        panelCentral.add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(ConstantesUI.FUENTE_NORMAL);
        panelCentral.add(lblPass, gbc);

        gbc.gridx = 1;
        campoContrasena = new JPasswordField(15);
        campoContrasena.setFont(ConstantesUI.FUENTE_NORMAL);
        panelCentral.add(campoContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        botonIngresar = new JButton("Ingresar");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(botonIngresar);
        panelCentral.add(botonIngresar, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters
    public JTextField getCampoUsuario() { return campoUsuario; }
    public JPasswordField getCampoContrasena() { return campoContrasena; }
    public JButton getBotonIngresar() { return botonIngresar; }
}