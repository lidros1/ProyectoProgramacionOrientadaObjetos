// Archivo: src/vista/ProyectoDetalleVista.java
package vista;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProyectoDetalleVista extends JFrame {
    private Proyecto proyecto;
    private JToggleButton btnVistaListaTareas;
    private JToggleButton btnVistaKanbanTareas;
    private JComboBox<Prioridad> comboFiltroPrioridadTareas;
    private JTextField txtBusquedaTareas;
    private JButton btnVolverAProyectos;
    private JPanel panelContenedorPrincipalTareas;
    private JPanel panelContenidoListaTareas;
    private Map<String, JPanel> columnasKanbanTareas;
    private DefaultListModel<Usuario> listModelUsuariosProyecto;

    public ProyectoDetalleVista(Proyecto proyecto, List<Prioridad> prioridadesDisponibles) {
        this.proyecto = proyecto;
        setTitle("Detalles del Proyecto: " + proyecto.getNombreProyecto());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 15));
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                ConstantesUI.BORDE_TITULO("Información del Proyecto"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelSuperior.setOpaque(false);

        // Izquierda: Info básica
        JPanel panelInfoIzquierda = new JPanel();
        panelInfoIzquierda.setOpaque(false);
        panelInfoIzquierda.setLayout(new BoxLayout(panelInfoIzquierda, BoxLayout.Y_AXIS));
        JLabel lblNombreProyecto = new JLabel("<html><b>" + proyecto.getNombreProyecto() + "</b></html>");
        lblNombreProyecto.setFont(ConstantesUI.FUENTE_TITULO);
        JLabel lblPrioridadEstado = new JLabel("Prioridad: " + proyecto.getNombrePrioridad() + " | Estado: " + proyecto.getNombreEstado());
        lblPrioridadEstado.setFont(ConstantesUI.FUENTE_NORMAL);
        panelInfoIzquierda.add(lblNombreProyecto);
        panelInfoIzquierda.add(Box.createVerticalStrut(5));
        panelInfoIzquierda.add(lblPrioridadEstado);

        // Centro: Lista de usuarios
        listModelUsuariosProyecto = new DefaultListModel<>();
        JList<Usuario> listaUsuariosProyecto = new JList<>(listModelUsuariosProyecto);
        listaUsuariosProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuariosProyecto);
        scrollUsuarios.setBorder(ConstantesUI.BORDE_TITULO("Equipo Designado"));

        // Derecha: Avance y botón
        JPanel panelInfoDerecha = new JPanel(new BorderLayout(10, 10));
        panelInfoDerecha.setOpaque(false);
        JProgressBar progressBarProyecto = new JProgressBar(0, 100);
        int progressValue = (proyecto.getPorcentajeAvance() != null) ? proyecto.getPorcentajeAvance().intValue() : 0;
        progressBarProyecto.setValue(progressValue);
        progressBarProyecto.setStringPainted(true);
        progressBarProyecto.setString("Avance: " + progressValue + "%");
        btnVolverAProyectos = new JButton("Volver a Proyectos");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolverAProyectos);
        panelInfoDerecha.add(new JLabel("Progreso General:"), BorderLayout.NORTH);
        panelInfoDerecha.add(progressBarProyecto, BorderLayout.CENTER);

        panelSuperior.add(panelInfoIzquierda, BorderLayout.WEST);
        panelSuperior.add(scrollUsuarios, BorderLayout.CENTER);
        panelSuperior.add(panelInfoDerecha, BorderLayout.EAST);

        JPanel panelContenedorSuperior = new JPanel(new BorderLayout());
        panelContenedorSuperior.setOpaque(false);
        panelContenedorSuperior.add(panelSuperior, BorderLayout.CENTER);
        panelContenedorSuperior.add(btnVolverAProyectos, BorderLayout.SOUTH);

        add(panelContenedorSuperior, BorderLayout.NORTH);

        // Panel Central: Tareas
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(ConstantesUI.BORDE_TITULO("Tareas del Proyecto"));
        panelCentral.setOpaque(false);

        JPanel panelFiltrosTareas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltrosTareas.setOpaque(false);
        btnVistaListaTareas = new JToggleButton("Lista", true);
        btnVistaKanbanTareas = new JToggleButton("Kanban");
        TemaPersonalizado.aplicarEstiloBotonAlternar(btnVistaListaTareas);
        TemaPersonalizado.aplicarEstiloBotonAlternar(btnVistaKanbanTareas);
        ButtonGroup grupoVistasTareas = new ButtonGroup();
        grupoVistasTareas.add(btnVistaListaTareas);
        grupoVistasTareas.add(btnVistaKanbanTareas);

        comboFiltroPrioridadTareas = new JComboBox<>();
        comboFiltroPrioridadTareas.setFont(ConstantesUI.FUENTE_NORMAL);
        Prioridad todasTareas = new Prioridad();
        todasTareas.setIdPrioridad(0);
        todasTareas.setNombrePrioridad("Todas");
        comboFiltroPrioridadTareas.addItem(todasTareas);
        prioridadesDisponibles.forEach(comboFiltroPrioridadTareas::addItem);

        txtBusquedaTareas = new JTextField(20);
        txtBusquedaTareas.setFont(ConstantesUI.FUENTE_NORMAL);

        panelFiltrosTareas.add(new JLabel("Vista Tareas:"));
        panelFiltrosTareas.add(btnVistaListaTareas);
        panelFiltrosTareas.add(btnVistaKanbanTareas);
        panelFiltrosTareas.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltrosTareas.add(new JLabel("Prioridad:"));
        panelFiltrosTareas.add(comboFiltroPrioridadTareas);
        panelFiltrosTareas.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltrosTareas.add(new JLabel("Buscar Tarea:"));
        panelFiltrosTareas.add(txtBusquedaTareas);
        panelCentral.add(panelFiltrosTareas, BorderLayout.NORTH);

        // Kanban de tareas
        panelContenedorPrincipalTareas = new JPanel(new CardLayout());
        panelContenedorPrincipalTareas.setOpaque(false);
        panelContenidoListaTareas = new JPanel();
        panelContenidoListaTareas.setLayout(new BoxLayout(panelContenidoListaTareas, BoxLayout.Y_AXIS));
        panelContenidoListaTareas.setOpaque(false);

        JPanel panelContenidoKanbanTareas = new JPanel(new GridLayout(1, 5, 10, 10));
        panelContenidoKanbanTareas.setOpaque(false);
        columnasKanbanTareas = new HashMap<>();
        String[] nombresColumnasTareas = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "BLOQUEADO", "HECHO"};
        for (String nombreColumna : nombresColumnasTareas) {
            JPanel columna = new JPanel();
            columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
            columna.setBorder(BorderFactory.createTitledBorder(nombreColumna));
            TemaPersonalizado.aplicarEstiloColumnaKanban(columna);
            columnasKanbanTareas.put(nombreColumna, columna);
            JScrollPane scroll = new JScrollPane(columna);
            scroll.setBorder(null);
            scroll.getViewport().setOpaque(false);
            panelContenidoKanbanTareas.add(scroll);
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