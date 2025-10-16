package vista;

import modelo.Prioridad;
import modelo.Proyecto; // <-- IMPORTAR
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarTareaVista extends JFrame {
    private JToggleButton btnVistaLista;
    private JToggleButton btnVistaKanban;
    private JComboBox<Proyecto> comboFiltroProyecto; // <-- NUEVO
    private JComboBox<Prioridad> comboFiltroPrioridad;
    private JTextField txtBusqueda;
    private JButton btnVolver;
    private JPanel panelContenedorPrincipal;
    private JPanel panelContenidoLista;
    private JPanel panelContenidoKanban;
    private Map<String, JPanel> columnasKanban;

    public ListarTareaVista(List<Prioridad> prioridades, List<Proyecto> proyectos) { // <-- CONSTRUCTOR MODIFICADO
        setTitle("Listado de Todas las Tareas");
        setSize(1300, 800); // Un poco más ancho para el nuevo filtro
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        btnVistaLista = new JToggleButton("Lista", true);
        btnVistaKanban = new JToggleButton("Kanban");
        ButtonGroup grupoVistas = new ButtonGroup();
        grupoVistas.add(btnVistaLista);
        grupoVistas.add(btnVistaKanban);

        // --- NUEVO FILTRO POR PROYECTO ---
        comboFiltroProyecto = new JComboBox<>();
        Proyecto todosLosProyectos = new Proyecto();
        todosLosProyectos.setIdProyecto(0);
        todosLosProyectos.setNombreProyecto("Todos los Proyectos");
        comboFiltroProyecto.addItem(todosLosProyectos);
        proyectos.forEach(comboFiltroProyecto::addItem);
        // ------------------------------------

        comboFiltroPrioridad = new JComboBox<>();
        Prioridad todasLasPrioridades = new Prioridad();
        todasLasPrioridades.setIdPrioridad(0);
        todasLasPrioridades.setNombrePrioridad("Todas las Prioridades");
        comboFiltroPrioridad.addItem(todasLasPrioridades);
        prioridades.forEach(comboFiltroPrioridad::addItem);

        txtBusqueda = new JTextField(15);
        btnVolver = new JButton("Volver");

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
        panelFiltros.add(btnVolver);

        add(panelFiltros, BorderLayout.NORTH);

        // Panel de Contenido con CardLayout
        panelContenedorPrincipal = new JPanel(new CardLayout());

        panelContenidoLista = new JPanel();
        panelContenidoLista.setLayout(new BoxLayout(panelContenidoLista, BoxLayout.Y_AXIS));

        panelContenidoKanban = new JPanel(new GridLayout(1, 5, 10, 10));
        columnasKanban = new HashMap<>();
        String[] nombresColumnas = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "BLOQUEADO", "HECHO"};
        for (String nombreColumna : nombresColumnas) {
            JPanel columna = new JPanel();
            columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
            columna.setBorder(BorderFactory.createTitledBorder(nombreColumna));
            columnasKanban.put(nombreColumna, columna);
            panelContenidoKanban.add(new JScrollPane(columna));
        }

        panelContenedorPrincipal.add(new JScrollPane(panelContenidoLista), "Lista");
        panelContenedorPrincipal.add(panelContenidoKanban, "Kanban");

        add(panelContenedorPrincipal, BorderLayout.CENTER);
    }

    // Getters
    public JToggleButton getBtnVistaLista() { return btnVistaLista; }
    public JToggleButton getBtnVistaKanban() { return btnVistaKanban; }
    public JComboBox<Proyecto> getComboFiltroProyecto() { return comboFiltroProyecto; } // <-- NUEVO
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public JPanel getPanelContenedorPrincipal() { return panelContenedorPrincipal; }
    public JPanel getPanelContenidoLista() { return panelContenidoLista; }
    public Map<String, JPanel> getColumnasKanban() { return columnasKanban; }
}