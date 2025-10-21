// Archivo: src/vista/CrearUsuarioVista.java
package vista;

import modelo.AreaSistema;
import modelo.Funcion;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CrearUsuarioVista extends JFrame {
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JButton btnCrear;
    private JButton btnVolver;
    private Map<String, JCheckBox> checkboxesPermisos;

    public CrearUsuarioVista(List<AreaSistema> areas, List<Funcion> funciones) {
        setTitle("Crear Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        checkboxesPermisos = new HashMap<>();

        // Panel de datos del usuario
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(ConstantesUI.BORDE_TITULO("Datos del Usuario"));
        panelDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; panelDatos.add(new JLabel("Nombre de Usuario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; txtNombreUsuario = new JTextField(20); txtNombreUsuario.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtContrasena = new JPasswordField(20); txtContrasena.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtContrasena, gbc);

        // Panel de permisos dinámico
        JPanel panelPermisos = new JPanel();
        panelPermisos.setLayout(new BoxLayout(panelPermisos, BoxLayout.Y_AXIS));
        panelPermisos.setBorder(ConstantesUI.BORDE_TITULO("Permisos"));
        panelPermisos.setOpaque(false);

        for (AreaSistema area : areas) {
            JPanel panelArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelArea.setOpaque(false);
            panelArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ConstantesUI.COLOR_BORDE));
            JLabel lblArea = new JLabel("<html><b>" + area.getNombreAreaSistema() + "</b></html>");
            lblArea.setFont(ConstantesUI.FUENTE_NORMAL);
            panelArea.add(lblArea);
            for (Funcion funcion : funciones) {
                String key = area.getIdAreaSistema() + "-" + funcion.getIdFuncion();
                JCheckBox checkBox = new JCheckBox(funcion.getNombreFuncion());
                checkBox.setFont(ConstantesUI.FUENTE_NORMAL);
                checkBox.setOpaque(false);
                checkboxesPermisos.put(key, checkBox);
                panelArea.add(checkBox);
            }
            panelPermisos.add(panelArea);
        }

        JScrollPane scrollPermisos = new JScrollPane(panelPermisos);
        scrollPermisos.setBorder(null);
        scrollPermisos.getViewport().setOpaque(false);

        JPanel panelCentral = new JPanel(new BorderLayout(10,10));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelCentral.add(panelDatos, BorderLayout.NORTH);
        panelCentral.add(scrollPermisos, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setOpaque(false);
        btnCrear = new JButton("Crear Usuario");
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrear);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelBotones.add(btnVolver);
        panelBotones.add(btnCrear);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtNombreUsuario() { return txtNombreUsuario; }
    public JPasswordField getTxtContrasena() { return txtContrasena; }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnVolver() { return btnVolver; }
    public Map<String, JCheckBox> getCheckboxesPermisos() { return checkboxesPermisos; }
}