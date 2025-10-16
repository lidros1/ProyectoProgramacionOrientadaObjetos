package vista;

import modelo.Prioridad;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisProyectosVista extends JFrame {
    private JComboBox<Prioridad> comboFiltroPrioridad;
    private JTextField txtBusqueda;
    private JButton btnVolver;
    private JPanel panelContenidoKanban;
    private Map<String, JPanel> columnasKanban;

    public MisProyectosVista(List<Prioridad> prioridades) {
        setTitle("Mis Proyectos Asignados");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Filtros (Superior) ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        comboFiltroPrioridad = new JComboBox<>();
        Prioridad todas = new Prioridad();
        todas.setIdPrioridad(0);
        todas.setNombrePrioridad("Todas");
        comboFiltroPrioridad.addItem(todas);
        for (Prioridad p : prioridades) {
            comboFiltroPrioridad.addItem(p);
        }

        txtBusqueda = new JTextField(20);
        btnVolver = new JButton("Volver");

        panelFiltros.add(new JLabel("Filtrar por Prioridad:"));
        panelFiltros.add(comboFiltroPrioridad);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Buscar por Nombre:"));
        panelFiltros.add(txtBusqueda);
        panelFiltros.add(btnVolver);

        add(panelFiltros, BorderLayout.NORTH);

        // --- Panel Kanban (Centro) ---
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

        add(panelContenidoKanban, BorderLayout.CENTER);
    }

    // Getters
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public Map<String, JPanel> getColumnasKanban() { return columnasKanban; }
}