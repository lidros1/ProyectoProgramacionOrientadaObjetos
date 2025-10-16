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
    private JTextField txtMail;
    private JButton btnCrear;
    private JButton btnVolver;
    private JPanel panelPermisos;
    private Map<String, JCheckBox> checkboxesPermisos;

    public CrearUsuarioVista(List<AreaSistema> areas, List<Funcion> funciones) {
        setTitle("Crear Nuevo Usuario");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        checkboxesPermisos = new HashMap<>();

        // Panel de datos del usuario
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDatos.add(new JLabel("Nombre de Usuario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtNombreUsuario = new JTextField(20); panelDatos.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtContrasena = new JPasswordField(20); panelDatos.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDatos.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtMail = new JTextField(20); panelDatos.add(txtMail, gbc);

        add(panelDatos, BorderLayout.NORTH);

        // Panel de permisos dinámico
        panelPermisos = new JPanel();
        panelPermisos.setLayout(new BoxLayout(panelPermisos, BoxLayout.Y_AXIS));
        panelPermisos.setBorder(BorderFactory.createTitledBorder("Permisos"));

        for (AreaSistema area : areas) {
            JPanel panelArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            panelArea.add(new JLabel("<html><b>" + area.getNombreAreaSistema() + "</b></html>"));
            for (Funcion funcion : funciones) {
                String key = area.getIdAreaSistema() + "-" + funcion.getIdFuncion();
                JCheckBox checkBox = new JCheckBox(funcion.getNombreFuncion());
                checkboxesPermisos.put(key, checkBox);
                panelArea.add(checkBox);
            }
            panelPermisos.add(panelArea);
        }

        add(new JScrollPane(panelPermisos), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrear = new JButton("Crear Usuario");
        btnVolver = new JButton("Volver");
        panelBotones.add(btnCrear);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtNombreUsuario() { return txtNombreUsuario; }
    public JPasswordField getTxtContrasena() { return txtContrasena; }
    public JTextField getTxtMail() { return txtMail; }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnVolver() { return btnVolver; }
    public Map<String, JCheckBox> getCheckboxesPermisos() { return checkboxesPermisos; }
}