// Archivo: src/vista/ListarUsuarioVista.java
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);

        // Panel Izquierdo
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(ConstantesUI.BORDE_TITULO("Buscar Usuario"));
        panelIzquierdo.setOpaque(false);

        txtBuscar = new JTextField();
        txtBuscar.setFont(ConstantesUI.FUENTE_NORMAL);
        panelIzquierdo.add(txtBuscar, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaResultados = new JList<>(listModel);
        listaResultados.setFont(ConstantesUI.FUENTE_NORMAL);
        JScrollPane scrollLista = new JScrollPane(listaResultados);
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);

        // Panel Derecho
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBorder(ConstantesUI.BORDE_TITULO("Detalles del Usuario"));
        panelDerecho.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; panelDerecho.add(new JLabel("<html><b>Nombre:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7; lblNombre = new JLabel("-"); lblNombre.setFont(ConstantesUI.FUENTE_NORMAL); panelDerecho.add(lblNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDerecho.add(new JLabel("<html><b>Email:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; lblMail = new JLabel("-"); lblMail.setFont(ConstantesUI.FUENTE_NORMAL); panelDerecho.add(lblMail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDerecho.add(new JLabel("<html><b>Fecha Creación:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; lblFechaCreacion = new JLabel("-"); lblFechaCreacion.setFont(ConstantesUI.FUENTE_NORMAL); panelDerecho.add(lblFechaCreacion, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDerecho.add(new JLabel("<html><b>Último Acceso:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; lblUltimoAcceso = new JLabel("-"); lblUltimoAcceso.setFont(ConstantesUI.FUENTE_NORMAL); panelDerecho.add(lblUltimoAcceso, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.SOUTHEAST; gbc.weighty = 1.0;
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelDerecho.add(btnVolver, gbc);

        // Contenedor Principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(350);
        splitPane.setOpaque(false);

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