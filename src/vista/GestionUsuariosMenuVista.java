// Archivo: src/vista/GestionUsuariosMenuVista.java
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

        btnCrearUsuario = new JButton("Crear Usuario");
        btnListarUsuario = new JButton("Listar Usuarios");
        btnEditarUsuario = new JButton("Editar Usuario y Permisos");
        btnVolver = new JButton("Volver al Menú Principal");

        // Aplicar estilos
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrearUsuario);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnListarUsuario);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnEditarUsuario);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        gbc.gridy = 0; panelCentral.add(btnCrearUsuario, gbc);
        gbc.gridy = 1; panelCentral.add(btnListarUsuario, gbc);
        gbc.gridy = 2; panelCentral.add(btnEditarUsuario, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridy = 3; panelCentral.add(btnVolver, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters
    public JButton getBtnCrearUsuario() { return btnCrearUsuario; }
    public JButton getBtnListarUsuario() { return btnListarUsuario; }
    public JButton getBtnEditarUsuario() { return btnEditarUsuario; }
    public JButton getBtnVolver() { return btnVolver; }
}