package vista;

import modelo.AreaSistema;
import modelo.Funcion;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarUsuarioVista extends JFrame {
    // Componentes de búsqueda
    private JTextField txtBuscar;
    private JList<Usuario> listaResultados;
    private DefaultListModel<Usuario> listModel;

    // Componentes del formulario de edición
    private JPanel panelEdicion;
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JTextField txtMail;
    private JPanel panelPermisos;
    private Map<String, JCheckBox> checkboxesPermisos;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JSplitPane splitPane;

    public EditarUsuarioVista(List<AreaSistema> areas, List<Funcion> funciones) {
        setTitle("Editar Usuario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        checkboxesPermisos = new HashMap<>();

        // --- Panel de Búsqueda (Izquierda) ---
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Seleccionar Usuario"));
        txtBuscar = new JTextField();
        panelBusqueda.add(txtBuscar, BorderLayout.NORTH);
        listModel = new DefaultListModel<>();
        listaResultados = new JList<>(listModel);
        panelBusqueda.add(new JScrollPane(listaResultados), BorderLayout.CENTER);

        // --- Panel de Edición (Derecha) ---
        panelEdicion = new JPanel(new BorderLayout(10, 10));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Editar Datos"));

        // Datos del usuario
        JPanel panelDatos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtNombreUsuario = new JTextField(20); panelDatos.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtMail = new JTextField(20); panelDatos.add(txtMail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDatos.add(new JLabel("Nueva Contraseña (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtContrasena = new JPasswordField(20); panelDatos.add(txtContrasena, gbc);

        panelEdicion.add(panelDatos, BorderLayout.NORTH);

        // Permisos
        panelPermisos = new JPanel();
        panelPermisos.setLayout(new BoxLayout(panelPermisos, BoxLayout.Y_AXIS));
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
        panelEdicion.add(new JScrollPane(panelPermisos), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar Cambios");
        btnVolver = new JButton("Volver");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnVolver);
        panelEdicion.add(panelBotones, BorderLayout.SOUTH);

        // Contenedor principal
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusqueda, panelEdicion);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);
        add(splitPane);
    }

    // Getters
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JList<Usuario> getListaResultados() { return listaResultados; }
    public DefaultListModel<Usuario> getListModel() { return listModel; }
    public JPanel getPanelEdicion() { return panelEdicion; }
    public JTextField getTxtNombreUsuario() { return txtNombreUsuario; }
    public JPasswordField getTxtContrasena() { return txtContrasena; }
    public JTextField getTxtMail() { return txtMail; }
    public Map<String, JCheckBox> getCheckboxesPermisos() { return checkboxesPermisos; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JSplitPane getSplitPane() { return splitPane; }
}