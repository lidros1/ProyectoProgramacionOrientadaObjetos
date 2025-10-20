// Archivo: src/vista/ListarTareaVista.java
package vista;

import modelo.Prioridad;
import modelo.Proyecto;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarTareaVista extends JFrame {
    private JToggleButton btnVistaLista;
    private JToggleButton btnVistaKanban;
    private JComboBox<Proyecto> comboFiltroProyecto;
    private JComboBox<Prioridad> comboFiltroPrioridad;
    private JTextField txtBusqueda;
    private JButton btnVolver;
    private JPanel panelContenedorPrincipal;
    private JPanel panelContenidoLista;
    private Map<String, JPanel> columnasKanban;

    public ListarTareaVista(List<Prioridad> prioridades, List<Proyecto> proyectos) {
        setTitle("Listado de Todas las Tareas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Corrección de tamaño y estilo
        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnVistaLista = new JToggleButton("Lista", true);
        btnVistaKanban = new JToggleButton("Kanban");
        TemaPersonalizado.aplicarEstiloBotonAlternar(btnVistaLista);
        TemaPersonalizado.aplicarEstiloBotonAlternar(btnVistaKanban);
        ButtonGroup grupoVistas = new ButtonGroup();
        grupoVistas.add(btnVistaLista);
        grupoVistas.add(btnVistaKanban);

        comboFiltroProyecto = new JComboBox<>();
        comboFiltroProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        Proyecto todosLosProyectos = new Proyecto();
        todosLosProyectos.setIdProyecto(0);
        todosLosProyectos.setNombreProyecto("Todos los Proyectos");
        comboFiltroProyecto.addItem(todosLosProyectos);
        proyectos.forEach(comboFiltroProyecto::addItem);

        comboFiltroPrioridad = new JComboBox<>();
        comboFiltroPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        Prioridad todasLasPrioridades = new Prioridad();
        todasLasPrioridades.setIdPrioridad(0);
        todasLasPrioridades.setNombrePrioridad("Todas las Prioridades");
        comboFiltroPrioridad.addItem(todasLasPrioridades);
        prioridades.forEach(comboFiltroPrioridad::addItem);

        txtBusqueda = new JTextField(15);
        txtBusqueda.setFont(ConstantesUI.FUENTE_NORMAL);
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        panelFiltros.add(new JLabel("Vista:"));
        panelFiltros.add(btnVistaLista);
        panelFiltros.add(btnVistaKanban);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Proyecto:"));
        panelFiltros.add(comboFiltroProyecto);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Prioridad:"));
        panelFiltros.add(comboFiltroPrioridad);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Buscar Tarea:"));
        panelFiltros.add(txtBusqueda);

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVolver.setOpaque(false);
        panelVolver.add(btnVolver);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(panelFiltros, BorderLayout.WEST);
        panelSuperior.add(panelVolver, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de Contenido
        panelContenedorPrincipal = new JPanel(new CardLayout());
        panelContenedorPrincipal.setOpaque(false);

        panelContenidoLista = new JPanel();
        panelContenidoLista.setLayout(new BoxLayout(panelContenidoLista, BoxLayout.Y_AXIS));
        panelContenidoLista.setOpaque(false);

        JPanel panelContenidoKanban = new JPanel(new GridLayout(1, 5, 10, 10));
        panelContenidoKanban.setOpaque(false);
        panelContenidoKanban.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        columnasKanban = new HashMap<>();
        String[] nombresColumnas = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "BLOQUEADO", "HECHO"};
        for (String nombreColumna : nombresColumnas) {
            JPanel columna = new JPanel();
            columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
            columna.setBorder(BorderFactory.createTitledBorder(nombreColumna));
            TemaPersonalizado.aplicarEstiloColumnaKanban(columna);
            columnasKanban.put(nombreColumna, columna);
            JScrollPane scrollColumna = new JScrollPane(columna);
            scrollColumna.setBorder(null);
            scrollColumna.getViewport().setOpaque(false);
            panelContenidoKanban.add(scrollColumna);
        }

        panelContenedorPrincipal.add(new JScrollPane(panelContenidoLista), "Lista");
        panelContenedorPrincipal.add(panelContenidoKanban, "Kanban");

        add(panelContenedorPrincipal, BorderLayout.CENTER);
    }

    // Getters
    public JToggleButton getBtnVistaLista() { return btnVistaLista; }
    public JToggleButton getBtnVistaKanban() { return btnVistaKanban; }
    public JComboBox<Proyecto> getComboFiltroProyecto() { return comboFiltroProyecto; }
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public JPanel getPanelContenedorPrincipal() { return panelContenedorPrincipal; }
    public JPanel getPanelContenidoLista() { return panelContenidoLista; }
    public Map<String, JPanel> getColumnasKanban() { return columnasKanban; }
}