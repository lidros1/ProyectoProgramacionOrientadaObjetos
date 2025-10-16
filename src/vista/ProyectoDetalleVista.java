package vista;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProyectoDetalleVista extends JFrame {
    private Proyecto proyecto;

    // --- Detalles del Proyecto ---
    private JLabel lblNombreProyecto;
    private JProgressBar progressBarProyecto;
    private JList<Usuario> listaUsuariosProyecto;
    private DefaultListModel<Usuario> listModelUsuariosProyecto;

    // --- Panel de Filtros de Tareas ---
    private JToggleButton btnVistaListaTareas;
    private JToggleButton btnVistaKanbanTareas;
    private JComboBox<Prioridad> comboFiltroPrioridadTareas;
    private JTextField txtBusquedaTareas;
    private JButton btnVolverAProyectos;

    // --- Panel de Contenido de Tareas (con CardLayout) ---
    private JPanel panelContenedorPrincipalTareas;
    private JPanel panelContenidoListaTareas;
    private JPanel panelContenidoKanbanTareas;
    private Map<String, JPanel> columnasKanbanTareas;

    public ProyectoDetalleVista(Proyecto proyecto, List<Prioridad> prioridadesDisponibles) {
        this.proyecto = proyecto;
        setTitle("Detalles del Proyecto: " + proyecto.getNombreProyecto());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Superior: Información General ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));

        // --- Panel Izquierdo Superior: Detalles de texto ---
        JPanel panelInfoIzquierda = new JPanel();
        panelInfoIzquierda.setLayout(new BoxLayout(panelInfoIzquierda, BoxLayout.Y_AXIS));
        lblNombreProyecto = new JLabel("<html><b>" + proyecto.getNombreProyecto() + "</b></html>");
        lblNombreProyecto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lblPrioridadEstado = new JLabel("Prioridad: " + proyecto.getNombrePrioridad() + " | Estado: " + proyecto.getNombreEstado());
        lblPrioridadEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        panelInfoIzquierda.add(lblNombreProyecto);
        panelInfoIzquierda.add(Box.createVerticalStrut(5));
        panelInfoIzquierda.add(lblPrioridadEstado);

        // --- Panel Central Superior: Lista de Usuarios ---
        JPanel panelUsuarios = new JPanel(new BorderLayout());
        listModelUsuariosProyecto = new DefaultListModel<>();
        listaUsuariosProyecto = new JList<>(listModelUsuariosProyecto);
        panelUsuarios.add(new JLabel("Usuarios Designados:"), BorderLayout.NORTH);
        panelUsuarios.add(new JScrollPane(listaUsuariosProyecto), BorderLayout.CENTER);

        // --- Panel Derecho Superior: Avance y Botón ---
        JPanel panelInfoDerecha = new JPanel(new BorderLayout(10, 5));
        progressBarProyecto = new JProgressBar(0, 100);
        BigDecimal avance = proyecto.getPorcentajeAvance();
        int progressValue = (avance != null) ? avance.intValue() : 0;
        progressBarProyecto.setValue(progressValue);
        progressBarProyecto.setStringPainted(true);
        progressBarProyecto.setString("Avance: " + progressValue + "%");
        btnVolverAProyectos = new JButton("Volver a Proyectos");
        panelInfoDerecha.add(progressBarProyecto, BorderLayout.NORTH);
        panelInfoDerecha.add(btnVolverAProyectos, BorderLayout.SOUTH);

        // Añadir sub-paneles al panel superior
        panelSuperior.add(panelInfoIzquierda, BorderLayout.WEST);
        panelSuperior.add(panelUsuarios, BorderLayout.CENTER);
        panelSuperior.add(panelInfoDerecha, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central: Tareas ---
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Tareas del Proyecto"));

        // Panel de Filtros de Tareas
        JPanel panelFiltrosTareas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        btnVistaListaTareas = new JToggleButton("Lista", true);
        btnVistaKanbanTareas = new JToggleButton("Kanban");
        ButtonGroup grupoVistasTareas = new ButtonGroup();
        grupoVistasTareas.add(btnVistaListaTareas);
        grupoVistasTareas.add(btnVistaKanbanTareas);

        comboFiltroPrioridadTareas = new JComboBox<>();
        Prioridad todasTareas = new Prioridad();
        todasTareas.setIdPrioridad(0);
        todasTareas.setNombrePrioridad("Todas");
        comboFiltroPrioridadTareas.addItem(todasTareas);
        for (Prioridad p : prioridadesDisponibles) {
            comboFiltroPrioridadTareas.addItem(p);
        }

        txtBusquedaTareas = new JTextField(20);

        panelFiltrosTareas.add(new JLabel("Vista Tareas:"));
        panelFiltrosTareas.add(btnVistaListaTareas);
        panelFiltrosTareas.add(btnVistaKanbanTareas);
        panelFiltrosTareas.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltrosTareas.add(new JLabel("Filtrar por Prioridad:"));
        panelFiltrosTareas.add(comboFiltroPrioridadTareas);
        panelFiltrosTareas.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltrosTareas.add(new JLabel("Buscar Tarea:"));
        panelFiltrosTareas.add(txtBusquedaTareas);

        panelCentral.add(panelFiltrosTareas, BorderLayout.NORTH);

        // Panel de Contenido Principal de Tareas con CardLayout
        panelContenedorPrincipalTareas = new JPanel(new CardLayout());

        panelContenidoListaTareas = new JPanel();
        panelContenidoListaTareas.setLayout(new BoxLayout(panelContenidoListaTareas, BoxLayout.Y_AXIS));

        panelContenidoKanbanTareas = new JPanel(new GridLayout(1, 5, 10, 10));
        columnasKanbanTareas = new HashMap<>();
        String[] nombresColumnasTareas = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "BLOQUEADO", "HECHO"};
        for (String nombreColumna : nombresColumnasTareas) {
            JPanel columna = new JPanel();
            columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
            columna.setBorder(BorderFactory.createTitledBorder(nombreColumna));
            columnasKanbanTareas.put(nombreColumna, columna);
            panelContenidoKanbanTareas.add(new JScrollPane(columna));
        }

        panelContenedorPrincipalTareas.add(new JScrollPane(panelContenidoListaTareas), "ListaTareas");
        panelContenedorPrincipalTareas.add(panelContenidoKanbanTareas, "KanbanTareas");

        panelCentral.add(panelContenedorPrincipalTareas, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters
    public Proyecto getProyecto() { return proyecto; }
    public JToggleButton getBtnVistaListaTareas() { return btnVistaListaTareas; }
    public JToggleButton getBtnVistaKanbanTareas() { return btnVistaKanbanTareas; }
    public JComboBox<Prioridad> getComboFiltroPrioridadTareas() { return comboFiltroPrioridadTareas; }
    public JTextField getTxtBusquedaTareas() { return txtBusquedaTareas; }
    public JButton getBtnVolverAProyectos() { return btnVolverAProyectos; }
    public JPanel getPanelContenedorPrincipalTareas() { return panelContenedorPrincipalTareas; }
    public JPanel getPanelContenidoListaTareas() { return panelContenidoListaTareas; }
    public Map<String, JPanel> getColumnasKanbanTareas() { return columnasKanbanTareas; }
    public DefaultListModel<Usuario> getListModelUsuariosProyecto() { return listModelUsuariosProyecto; }
}