// Archivo: src/vista/MisProyectosVista.java
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
    private Map<String, JPanel> columnasKanban;

    public MisProyectosVista(List<Prioridad> prioridades) {
        setTitle("Mis Proyectos Asignados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Corrección de tamaño y estilo
        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        comboFiltroPrioridad = new JComboBox<>();
        comboFiltroPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        Prioridad todas = new Prioridad();
        todas.setIdPrioridad(0);
        todas.setNombrePrioridad("Todas las Prioridades");
        comboFiltroPrioridad.addItem(todas);
        prioridades.forEach(comboFiltroPrioridad::addItem);

        txtBusqueda = new JTextField(20);
        txtBusqueda.setFont(ConstantesUI.FUENTE_NORMAL);
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        panelFiltros.add(new JLabel("Prioridad:"));
        panelFiltros.add(comboFiltroPrioridad);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Buscar:"));
        panelFiltros.add(txtBusqueda);

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVolver.setOpaque(false);
        panelVolver.add(btnVolver);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(panelFiltros, BorderLayout.WEST);
        panelSuperior.add(panelVolver, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel Kanban
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

        add(panelContenidoKanban, BorderLayout.CENTER);
    }

    // Getters
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public Map<String, JPanel> getColumnasKanban() { return columnasKanban; }
}