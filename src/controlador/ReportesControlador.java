// Archivo: src/controlador/ReportesControlador.java
package controlador;

import modelo.Proyecto;
import modelo.Tarea;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import persistencia.ReporteDAO;
import vista.ReportesVista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ReportesControlador implements ActionListener {
    private final ReportesVista vista;
    private final JFrame vistaAnterior;
    private final String tipoReporte;
    private final ReporteDAO reporteDAO;

    public ReportesControlador(ReportesVista vista, JFrame vistaAnterior, String tipoReporte) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.tipoReporte = tipoReporte;
        this.reporteDAO = new ReporteDAO();
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        generarReportes();
        vista.setVisible(true);
    }

    private void generarReportes() {
        // --- SOLUCIÓN: Cambiar el layout a BoxLayout para apilar verticalmente ---
        JPanel panelContenedor = vista.getPanelCentral();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));

        switch (tipoReporte) {
            case "PROYECTOS":
                vista.setTitle("Reportes de Proyectos");
                generarReportesDeProyectos();
                break;
            case "TAREAS":
                vista.setTitle("Reportes de Tareas");
                generarReportesDeTareas();
                break;
            case "USUARIOS":
                vista.setTitle("Reportes de Usuarios");
                generarReportesDeUsuarios();
                break;
        }
        // Forzar la revalidación del layout
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    // --- Método de ayuda para añadir componentes y controlar el tamaño ---
    private void agregarComponenteReporte(JComponent componente) {
        // Se establece un tamaño máximo para evitar que los gráficos se estiren verticalmente
        componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        componente.setAlignmentX(Component.CENTER_ALIGNMENT);
        vista.getPanelCentral().add(componente);
        vista.getPanelCentral().add(Box.createVerticalStrut(20)); // Espacio entre reportes
    }

    private void generarReportesDeProyectos() {
        // Reporte 1: Estado General
        Map<String, Integer> proyectosPorEstado = reporteDAO.contarProyectosPorEstado();
        JFreeChart pieChartEstados = crearGraficoTorta("Proyectos por Estado", proyectosPorEstado);
        agregarComponenteReporte(new ChartPanel(pieChartEstados));

        // Reporte 2: Proyectos por Prioridad
        Map<String, Integer> proyectosPorPrio = reporteDAO.contarProyectosPorPrioridad();
        JFreeChart barChartPrio = crearGraficoBarras("Proyectos por Prioridad", "Prioridad", "Cantidad", proyectosPorPrio);
        agregarComponenteReporte(new ChartPanel(barChartPrio));

        // Reporte 3: Proyectos Retrasados
        List<Proyecto> proyectosRetrasados = reporteDAO.listarProyectosRetrasados();
        JScrollPane scrollPane = new JScrollPane(crearTablaProyectosRetrasados(proyectosRetrasados));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Proyectos Retrasados"));
        agregarComponenteReporte(scrollPane);
    }

    private void generarReportesDeTareas() {
        // Reporte 1: Tareas por Estado
        Map<String, Integer> tareasPorEstado = reporteDAO.contarTareasPorEstado();
        JFreeChart pieChart = crearGraficoTorta("Tareas por Estado", tareasPorEstado);
        agregarComponenteReporte(new ChartPanel(pieChart));

        // Reporte 2: Tareas Vencidas
        List<Tarea> tareasVencidas = reporteDAO.listarTareasVencidas();
        JScrollPane scrollPane = new JScrollPane(crearTablaTareasVencidas(tareasVencidas));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tareas Vencidas"));
        agregarComponenteReporte(scrollPane);
    }

    private void generarReportesDeUsuarios() {
        // Reporte 1: Carga de Trabajo
        Map<String, Integer> cargaUsuarios = reporteDAO.contarTareasActivasPorUsuario();
        JFreeChart barChartCarga = crearGraficoBarras("Carga de Tareas Activas por Usuario", "Usuario", "N° Tareas", cargaUsuarios);
        agregarComponenteReporte(new ChartPanel(barChartCarga));

        // Reporte 2: Rendimiento de Usuarios
        Map<String, Integer> rendimientoUsuarios = reporteDAO.contarTareasCompletadasPorUsuario();
        JFreeChart barChartRendimiento = crearGraficoBarras("Tareas Completadas por Usuario", "Usuario", "N° Tareas", rendimientoUsuarios);
        agregarComponenteReporte(new ChartPanel(barChartRendimiento));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    // --- MÉTODOS DE AYUDA PARA CREAR GRÁFICOS Y TABLAS (sin cambios) ---

    private JFreeChart crearGraficoTorta(String titulo, Map<String, Integer> datos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSimpleLabels(true);
        return chart;
    }

    private JFreeChart crearGraficoBarras(String titulo, String categoria, String valor, Map<String, Integer> datos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), categoria);
        }
        return ChartFactory.createBarChart(titulo, categoria, valor, dataset);
    }

    private JTable crearTablaProyectosRetrasados(List<Proyecto> proyectos) {
        String[] columnas = {"Nombre Proyecto", "Fecha Fin Estimada", "Estado Actual"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Proyecto p : proyectos) {
            model.addRow(new Object[]{
                    p.getNombreProyecto(),
                    sdf.format(p.getFechaFinalEstimada()),
                    p.getNombreEstado()
            });
        }
        return new JTable(model);
    }

    private JTable crearTablaTareasVencidas(List<Tarea> tareas) {
        String[] columnas = {"Nombre Tarea", "Fecha Fin Estimada", "Estado Actual"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Tarea t : tareas) {
            model.addRow(new Object[]{
                    t.getNombreTarea(),
                    sdf.format(t.getFechaFinalEstimada()),
                    t.getNombreEstado()
            });
        }
        return new JTable(model);
    }
}