// Archivo: src/vista/AsignarTareaVista.java
package vista;

import modelo.Proyecto;
import modelo.Tarea;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class AsignarTareaVista extends JFrame {
    private JPanel panelIzquierdo;
    private CardLayout cardLayoutIzquierdo;
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> modelProyectos;
    private JButton btnVolverAProyectos;
    private JList<Tarea> listaTareas;
    private DefaultListModel<Tarea> modelTareas;
    private JPanel panelAsignacion;
    private JLabel lblTareaSeleccionada;
    private JList<Usuario> listaUsuariosDisponibles;
    private JList<Usuario> listaUsuariosAsignados;
    private DefaultListModel<Usuario> modelDisponibles;
    private DefaultListModel<Usuario> modelAsignados;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JSplitPane splitPane;

    public AsignarTareaVista() {
        setTitle("Asignar Usuarios a Tareas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Izquierdo (con CardLayout) ---
        cardLayoutIzquierdo = new CardLayout();
        panelIzquierdo = new JPanel(cardLayoutIzquierdo);
        panelIzquierdo.setOpaque(false);

        // Card 1: Proyectos
        JPanel panelProyectos = new JPanel(new BorderLayout(5, 5));
        panelProyectos.setOpaque(false);
        panelProyectos.setBorder(ConstantesUI.BORDE_TITULO("1. Seleccionar Proyecto"));
        txtBuscarProyecto = new JTextField();
        txtBuscarProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        modelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(modelProyectos);
        listaProyectos.setFont(ConstantesUI.FUENTE_NORMAL);
        panelProyectos.add(txtBuscarProyecto, BorderLayout.NORTH);
        panelProyectos.add(new JScrollPane(listaProyectos), BorderLayout.CENTER);
        panelProyectos.add(new JLabel("Doble clic para seleccionar", SwingConstants.CENTER), BorderLayout.SOUTH);

        // Card 2: Tareas
        JPanel panelTareas = new JPanel(new BorderLayout(5, 5));
        panelTareas.setOpaque(false);
        panelTareas.setBorder(ConstantesUI.BORDE_TITULO("2. Seleccionar Tarea"));
        btnVolverAProyectos = new JButton("<- Volver a Proyectos");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolverAProyectos);
        modelTareas = new DefaultListModel<>();
        listaTareas = new JList<>(modelTareas);
        listaTareas.setFont(ConstantesUI.FUENTE_NORMAL);
        panelTareas.add(btnVolverAProyectos, BorderLayout.NORTH);
        panelTareas.add(new JScrollPane(listaTareas), BorderLayout.CENTER);
        panelTareas.add(new JLabel("Doble clic para seleccionar", SwingConstants.CENTER), BorderLayout.SOUTH);

        panelIzquierdo.add(panelProyectos, "PROYECTOS");
        panelIzquierdo.add(panelTareas, "TAREAS");

        // --- Panel Derecho: Asignación ---
        panelAsignacion = new JPanel(new BorderLayout(10, 10));
        panelAsignacion.setBorder(ConstantesUI.BORDE_TITULO("3. Asignar Equipo"));
        panelAsignacion.setOpaque(false);
        panelAsignacion.setVisible(false);

        lblTareaSeleccionada = new JLabel(" ");
        lblTareaSeleccionada.setFont(ConstantesUI.FUENTE_TITULO);
        panelAsignacion.add(lblTareaSeleccionada, BorderLayout.NORTH);

        modelDisponibles = new DefaultListModel<>();
        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        listaUsuariosDisponibles.setFont(ConstantesUI.FUENTE_NORMAL);
        modelAsignados = new DefaultListModel<>();
        listaUsuariosAsignados = new JList<>(modelAsignados);
        listaUsuariosAsignados.setFont(ConstantesUI.FUENTE_NORMAL);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.setOpaque(false);

        JPanel panelDisponibles = new JPanel(new BorderLayout());
        panelDisponibles.setOpaque(false);
        panelDisponibles.add(new JLabel("Disponibles (Doble clic):"), BorderLayout.NORTH);
        panelDisponibles.add(new JScrollPane(listaUsuariosDisponibles), BorderLayout.CENTER);

        JPanel panelAsignados = new JPanel(new BorderLayout());
        panelAsignados.setOpaque(false);
        panelAsignados.add(new JLabel("Asignados (Doble clic):"), BorderLayout.NORTH);
        panelAsignados.add(new JScrollPane(listaUsuariosAsignados), BorderLayout.CENTER);

        panelListas.add(panelDisponibles);
        panelListas.add(panelAsignados);
        panelAsignacion.add(panelListas, BorderLayout.CENTER);

        btnGuardar = new JButton("Guardar Designaciones");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGuardar);
        JPanel panelGuardar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelGuardar.setOpaque(false);
        panelGuardar.add(btnGuardar);
        panelAsignacion.add(panelGuardar, BorderLayout.SOUTH);

        // Layout Principal
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelAsignacion);
        splitPane.setDividerLocation(350);
        splitPane.setOpaque(false);

        btnVolver = new JButton("Volver al Menú");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSur.setOpaque(false);
        panelSur.add(btnVolver);

        add(splitPane, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }

    // Getters
    public CardLayout getCardLayoutIzquierdo() { return cardLayoutIzquierdo; }
    public JPanel getPanelIzquierdo() { return panelIzquierdo; }
    public JTextField getTxtBuscarProyecto() { return txtBuscarProyecto; }
    public JList<Proyecto> getListaProyectos() { return listaProyectos; }
    public DefaultListModel<Proyecto> getModelProyectos() { return modelProyectos; }
    public JButton getBtnVolverAProyectos() { return btnVolverAProyectos; }
    public JList<Tarea> getListaTareas() { return listaTareas; }
    public DefaultListModel<Tarea> getModelTareas() { return modelTareas; }
    public JPanel getPanelAsignacion() { return panelAsignacion; }
    public JLabel getLblTareaSeleccionada() { return lblTareaSeleccionada; }
    public JList<Usuario> getListaUsuariosDisponibles() { return listaUsuariosDisponibles; }
    public DefaultListModel<Usuario> getModelDisponibles() { return modelDisponibles; }
    public JList<Usuario> getListaUsuariosAsignados() { return listaUsuariosAsignados; }
    public DefaultListModel<Usuario> getModelAsignados() { return modelAsignados; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JSplitPane getSplitPane() { return splitPane; }
}