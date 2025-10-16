package vista;

import modelo.Prioridad; // <-- SE USA EL MODELO PRINCIPAL
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarProyectoVista extends JFrame {
    private JToggleButton btnVistaLista;
    private JToggleButton btnVistaKanban;
    private JComboBox<Prioridad> comboFiltroPrioridad; // <-- Usa modelo.Prioridad
    private JTextField txtBusqueda;
    private JButton btnVolver;
    private JPanel panelContenedorPrincipal;
    private JPanel panelContenidoLista;
    private JPanel panelContenidoKanban;
    private Map<String, JPanel> columnasKanban;

    public ListarProyectoVista(List<Prioridad> prioridades) {
        setTitle("Listado de Proyectos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        btnVistaLista = new JToggleButton("Lista", true);
        btnVistaKanban = new JToggleButton("Kanban");
        ButtonGroup grupoVistas = new ButtonGroup();
        grupoVistas.add(btnVistaLista);
        grupoVistas.add(btnVistaKanban);

        comboFiltroPrioridad = new JComboBox<>();
        Prioridad todas = new Prioridad(); // Se crea un objeto Prioridad para "Todas"
        todas.setIdPrioridad(0);
        todas.setNombrePrioridad("Todas");
        comboFiltroPrioridad.addItem(todas);
        for (Prioridad p : prioridades) {
            comboFiltroPrioridad.addItem(p);
        }

        txtBusqueda = new JTextField(20);
        btnVolver = new JButton("Volver");

        panelFiltros.add(new JLabel("Vista:"));
        panelFiltros.add(btnVistaLista);
        panelFiltros.add(btnVistaKanban);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Filtrar por Prioridad:"));
        panelFiltros.add(comboFiltroPrioridad);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Buscar por Nombre:"));
        panelFiltros.add(txtBusqueda);
        panelFiltros.add(btnVolver);

        add(panelFiltros, BorderLayout.NORTH);

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

    public JToggleButton getBtnVistaLista() { return btnVistaLista; }
    public JToggleButton getBtnVistaKanban() { return btnVistaKanban; }
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public JPanel getPanelContenedorPrincipal() { return panelContenedorPrincipal; }
    public JPanel getPanelContenidoLista() { return panelContenidoLista; }
    public Map<String, JPanel> getColumnasKanban() { return columnasKanban; }

    // --- SE ELIMINÓ LA CLASE INTERNA 'Prioridad' ---
}