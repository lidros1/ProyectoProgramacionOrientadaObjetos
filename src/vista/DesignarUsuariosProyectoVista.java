// Archivo: src/vista/DesignarUsuariosProyectoVista.java
package vista;

import modelo.Proyecto;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class DesignarUsuariosProyectoVista extends JFrame {
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> listModelProyectos;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel de Búsqueda (Izquierda)
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.setBorder(ConstantesUI.BORDE_TITULO("1. Seleccionar Proyecto"));
        panelBusqueda.setOpaque(false);
        txtBuscarProyecto = new JTextField();
        txtBuscarProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        panelBusqueda.add(txtBuscarProyecto, BorderLayout.NORTH);
        listModelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(listModelProyectos);
        listaProyectos.setFont(ConstantesUI.FUENTE_NORMAL);
        panelBusqueda.add(new JScrollPane(listaProyectos), BorderLayout.CENTER);

        // Panel de Asignación (Derecha)
        panelAsignacion = new JPanel(new BorderLayout(10, 10));
        panelAsignacion.setBorder(ConstantesUI.BORDE_TITULO("2. Asignar Equipo"));
        panelAsignacion.setOpaque(false);
        panelAsignacion.setVisible(false);

        lblProyectoSeleccionado = new JLabel(" ");
        lblProyectoSeleccionado.setFont(ConstantesUI.FUENTE_TITULO);
        lblProyectoSeleccionado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelAsignacion.add(lblProyectoSeleccionado, BorderLayout.NORTH);

        modelDisponibles = new DefaultListModel<>();
        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        listaUsuariosDisponibles.setFont(ConstantesUI.FUENTE_NORMAL);
        modelAsignados = new DefaultListModel<>();
        listaUsuariosAsignados = new JList<>(modelAsignados);
        listaUsuariosAsignados.setFont(ConstantesUI.FUENTE_NORMAL);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.setOpaque(false);
        JPanel panelListaDisponibles = new JPanel(new BorderLayout(5,5));
        panelListaDisponibles.setOpaque(false);
        panelListaDisponibles.add(new JLabel("Disponibles (Doble clic para agregar):"), BorderLayout.NORTH);
        panelListaDisponibles.add(new JScrollPane(listaUsuariosDisponibles), BorderLayout.CENTER);

        JPanel panelListaAsignados = new JPanel(new BorderLayout(5,5));
        panelListaAsignados.setOpaque(false);
        panelListaAsignados.add(new JLabel("Asignados (Doble clic para quitar):"), BorderLayout.NORTH);
        panelListaAsignados.add(new JScrollPane(listaUsuariosAsignados), BorderLayout.CENTER);

        panelListas.add(panelListaDisponibles);
        panelListas.add(panelListaAsignados);
        panelAsignacion.add(panelListas, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setOpaque(false);
        btnGuardar = new JButton("Guardar Designaciones");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGuardar);
        panelAcciones.add(btnGuardar);
        panelAsignacion.add(panelAcciones, BorderLayout.SOUTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusqueda, panelAsignacion);
        splitPane.setDividerLocation(350);
        splitPane.setOpaque(false);

        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelVolver.setOpaque(false);
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