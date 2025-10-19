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
        vista.getPanelCentral().setLayout(new GridLayout(0, 2, 10, 10)); // Layout para múltiples reportes

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
        vista.revalidate();
        vista.repaint();
    }

    private void generarReportesDeProyectos() {
        // Reporte 1: Estado General de Proyectos
        Map<String, Integer> proyectosPorEstado = reporteDAO.contarProyectosPorEstado();
        JFreeChart pieChartEstados = crearGraficoTorta("Proyectos por Estado", proyectosPorEstado);
        vista.getPanelCentral().add(new ChartPanel(pieChartEstados));

        // Reporte 2: Proyectos por Prioridad
        Map<String, Integer> proyectosPorPrio = reporteDAO.contarProyectosPorPrioridad();
        JFreeChart barChartPrio = crearGraficoBarras("Proyectos por Prioridad", "Prioridad", "Cantidad", proyectosPorPrio);
        vista.getPanelCentral().add(new ChartPanel(barChartPrio));

        // Reporte 3: Proyectos en Riesgo
        List<Proyecto> proyectosRetrasados = reporteDAO.listarProyectosRetrasados();
        JTable tablaRetrasados = crearTablaProyectosRetrasados(proyectosRetrasados);
        JScrollPane scrollPane = new JScrollPane(tablaRetrasados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Proyectos Retrasados"));
        vista.getPanelCentral().add(scrollPane);
    }

    private void generarReportesDeTareas() {
        // Reporte 1: Tareas por Estado
        Map<String, Integer> tareasPorEstado = reporteDAO.contarTareasPorEstado();
        JFreeChart pieChart = crearGraficoTorta("Tareas por Estado", tareasPorEstado);
        vista.getPanelCentral().add(new ChartPanel(pieChart));

        // Reporte 2: Tareas Vencidas
        List<Tarea> tareasVencidas = reporteDAO.listarTareasVencidas();
        JTable tablaVencidas = crearTablaTareasVencidas(tareasVencidas);
        JScrollPane scrollPane = new JScrollPane(tablaVencidas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tareas Vencidas"));
        vista.getPanelCentral().add(scrollPane);
    }

    private void generarReportesDeUsuarios() {
        // Reporte 1: Carga de Trabajo
        Map<String, Integer> cargaUsuarios = reporteDAO.contarTareasActivasPorUsuario();
        JFreeChart barChartCarga = crearGraficoBarras("Carga de Tareas Activas por Usuario", "Usuario", "N° Tareas", cargaUsuarios);
        vista.getPanelCentral().add(new ChartPanel(barChartCarga));

        // Reporte 2: Rendimiento de Usuarios
        Map<String, Integer> rendimientoUsuarios = reporteDAO.contarTareasCompletadasPorUsuario();
        JFreeChart barChartRendimiento = crearGraficoBarras("Tareas Completadas por Usuario", "Usuario", "N° Tareas", rendimientoUsuarios);
        vista.getPanelCentral().add(new ChartPanel(barChartRendimiento));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    // --- MÉTODOS DE AYUDA PARA CREAR GRÁFICOS Y TABLAS ---

    private JFreeChart crearGraficoTorta(String titulo, Map<String, Integer> datos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSimpleLabels(true); // Evita que las etiquetas se superpongan
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
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
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
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
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