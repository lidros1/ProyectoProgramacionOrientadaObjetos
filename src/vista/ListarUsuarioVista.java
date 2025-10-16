package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class ListarUsuarioVista extends JFrame {
    private JTextField txtBuscar;
    private JList<Usuario> listaResultados;
    private DefaultListModel<Usuario> listModel;
    private JLabel lblNombre, lblMail, lblFechaCreacion, lblUltimoAcceso;
    private JButton btnVolver;

    public ListarUsuarioVista() {
        setTitle("Listar y Ver Usuarios");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Panel de Búsqueda y Resultados ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtBuscar = new JTextField();
        panelIzquierdo.add(new JLabel("Buscar por nombre:"), BorderLayout.NORTH);
        panelIzquierdo.add(txtBuscar, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        listaResultados = new JList<>(listModel);
        JScrollPane scrollLista = new JScrollPane(listaResultados);
        panelIzquierdo.add(scrollLista, BorderLayout.SOUTH);

        // --- Panel de Detalles ---
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Detalles del Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelDerecho.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; lblNombre = new JLabel("-"); panelDerecho.add(lblNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDerecho.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; lblMail = new JLabel("-"); panelDerecho.add(lblMail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDerecho.add(new JLabel("Fecha Creación:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; lblFechaCreacion = new JLabel("-"); panelDerecho.add(lblFechaCreacion, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDerecho.add(new JLabel("Último Acceso:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; lblUltimoAcceso = new JLabel("-"); panelDerecho.add(lblUltimoAcceso, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        btnVolver = new JButton("Volver");
        panelDerecho.add(btnVolver, gbc);

        // --- Contenedor Principal (Split Pane) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(250);

        add(splitPane);
    }

    // Getters
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JList<Usuario> getListaResultados() { return listaResultados; }
    public DefaultListModel<Usuario> getListModel() { return listModel; }
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblMail() { return lblMail; }
    public JLabel getLblFechaCreacion() { return lblFechaCreacion; }
    public JLabel getLblUltimoAcceso() { return lblUltimoAcceso; }
    public JButton getBtnVolver() { return btnVolver; }
}