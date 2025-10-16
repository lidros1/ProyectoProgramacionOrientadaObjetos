package vista;

import modelo.Proyecto;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class DesignarUsuariosProyectoVista extends JFrame {
    // Componentes de búsqueda de proyecto
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> listModelProyectos;

    // Componentes de asignación de usuarios
    private JPanel panelAsignacion;
    private JLabel lblProyectoSeleccionado;
    private JList<Usuario> listaUsuariosDisponibles;
    private JList<Usuario> listaUsuariosAsignados;
    private DefaultListModel<Usuario> modelDisponibles;
    private DefaultListModel<Usuario> modelAsignados;
    private JButton btnGuardar, btnVolver;
    private JSplitPane splitPane;

    public DesignarUsuariosProyectoVista() {
        setTitle("Designar Usuarios a Proyectos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Panel de Búsqueda de Proyecto (Izquierda) ---
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("1. Seleccionar Proyecto"));
        txtBuscarProyecto = new JTextField();
        panelBusqueda.add(txtBuscarProyecto, BorderLayout.NORTH);
        listModelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(listModelProyectos);
        panelBusqueda.add(new JScrollPane(listaProyectos), BorderLayout.CENTER);

        // --- Panel de Asignación (Derecha) ---
        panelAsignacion = new JPanel(new BorderLayout(10, 10));
        panelAsignacion.setBorder(BorderFactory.createTitledBorder("2. Asignar Equipo"));
        panelAsignacion.setVisible(false);

        lblProyectoSeleccionado = new JLabel(" ");
        lblProyectoSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelAsignacion.add(lblProyectoSeleccionado, BorderLayout.NORTH);

        modelDisponibles = new DefaultListModel<>();
        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        modelAsignados = new DefaultListModel<>();
        listaUsuariosAsignados = new JList<>(modelAsignados);

        // --- MODIFICACIÓN: Layout de 2 columnas sin buscadores de usuario ---
        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelListaDisponibles = new JPanel(new BorderLayout(5,5));
        panelListaDisponibles.add(new JLabel("Disponibles (Doble clic para agregar):"), BorderLayout.NORTH);
        panelListaDisponibles.add(new JScrollPane(listaUsuariosDisponibles), BorderLayout.CENTER);

        JPanel panelListaAsignados = new JPanel(new BorderLayout(5,5));
        panelListaAsignados.add(new JLabel("Asignados (Doble clic para quitar):"), BorderLayout.NORTH);
        panelListaAsignados.add(new JScrollPane(listaUsuariosAsignados), BorderLayout.CENTER);

        panelListas.add(panelListaDisponibles);
        panelListas.add(panelListaAsignados);

        panelAsignacion.add(panelListas, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar Designaciones");
        panelAcciones.add(btnGuardar);
        panelAsignacion.add(panelAcciones, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusqueda, panelAsignacion);
        splitPane.setDividerLocation(300);

        btnVolver = new JButton("Volver");
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVolver.add(btnVolver);

        add(splitPane, BorderLayout.CENTER);
        add(panelVolver, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtBuscarProyecto() { return txtBuscarProyecto; }
    public JList<Proyecto> getListaProyectos() { return listaProyectos; }
    public DefaultListModel<Proyecto> getListModelProyectos() { return listModelProyectos; }
    public JPanel getPanelAsignacion() { return panelAsignacion; }
    public JLabel getLblProyectoSeleccionado() { return lblProyectoSeleccionado; }
    public DefaultListModel<Usuario> getModelDisponibles() { return modelDisponibles; }
    public DefaultListModel<Usuario> getModelAsignados() { return modelAsignados; }
    public JList<Usuario> getListaUsuariosDisponibles() { return listaUsuariosDisponibles; }
    public JList<Usuario> getListaUsuariosAsignados() { return listaUsuariosAsignados; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JSplitPane getSplitPane() { return splitPane; }
}