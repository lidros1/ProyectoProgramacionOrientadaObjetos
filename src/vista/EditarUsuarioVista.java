// Archivo: src/vista/EditarUsuarioVista.java
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
    private JTextField txtBuscar;
    private JList<Usuario> listaResultados;
    private DefaultListModel<Usuario> listModel;
    private JPanel panelEdicion;
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboActivo;
    private Map<String, JCheckBox> checkboxesPermisos;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JSplitPane splitPane;

    public EditarUsuarioVista(List<AreaSistema> areas, List<Funcion> funciones) {
        setTitle("Editar Usuario y Permisos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        checkboxesPermisos = new HashMap<>();

        // Panel de Búsqueda (Izquierda)
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(ConstantesUI.BORDE_TITULO("1. Seleccionar Usuario"));
        panelBusqueda.setOpaque(false);
        txtBuscar = new JTextField();
        txtBuscar.setFont(ConstantesUI.FUENTE_NORMAL);
        panelBusqueda.add(txtBuscar, BorderLayout.NORTH);
        listModel = new DefaultListModel<>();
        listaResultados = new JList<>(listModel);
        listaResultados.setFont(ConstantesUI.FUENTE_NORMAL);
        panelBusqueda.add(new JScrollPane(listaResultados), BorderLayout.CENTER);

        // Panel de Edición (Derecha)
        panelEdicion = new JPanel(new BorderLayout(10, 10));
        panelEdicion.setBorder(ConstantesUI.BORDE_TITULO("2. Editar Datos y Permisos"));
        panelEdicion.setOpaque(false);

        // Datos del usuario
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtNombreUsuario = new JTextField(20); txtNombreUsuario.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Nueva Contraseña (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtContrasena = new JPasswordField(20); txtContrasena.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        comboActivo = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        comboActivo.setFont(ConstantesUI.FUENTE_NORMAL);
        panelDatos.add(comboActivo, gbc);

        panelEdicion.add(panelDatos, BorderLayout.NORTH);

        // Permisos
        JPanel panelPermisos = new JPanel();
        panelPermisos.setLayout(new BoxLayout(panelPermisos, BoxLayout.Y_AXIS));
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
        panelEdicion.add(scrollPermisos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setOpaque(false);
        btnGuardar = new JButton("Guardar Cambios");
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGuardar);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelBotones.add(btnVolver);
        panelBotones.add(btnGuardar);

        JPanel panelSurGlobal = new JPanel(new BorderLayout());
        panelSurGlobal.setOpaque(false);
        panelSurGlobal.add(panelBotones, BorderLayout.CENTER);

        panelEdicion.add(panelSurGlobal, BorderLayout.SOUTH);

        // Contenedor principal
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusqueda, panelEdicion);
        splitPane.setDividerLocation(350);
        splitPane.setOpaque(false);
        add(splitPane);
    }

    // Getters
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JList<Usuario> getListaResultados() { return listaResultados; }
    public DefaultListModel<Usuario> getListModel() { return listModel; }
    public JPanel getPanelEdicion() { return panelEdicion; }
    public JTextField getTxtNombreUsuario() { return txtNombreUsuario; }
    public JPasswordField getTxtContrasena() { return txtContrasena; }
    public JComboBox<String> getComboActivo() { return comboActivo; }
    public Map<String, JCheckBox> getCheckboxesPermisos() { return checkboxesPermisos; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JSplitPane getSplitPane() { return splitPane; }
}