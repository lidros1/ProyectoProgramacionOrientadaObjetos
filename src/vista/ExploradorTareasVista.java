// Archivo: src/vista/ExploradorTareasVista.java
package vista;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Tarea;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploradorTareasVista extends JFrame {
    private JComboBox<Proyecto> comboFiltroProyecto;
    private JComboBox<Prioridad> comboFiltroPrioridad;
    private JTextField txtBusqueda;
    private JButton btnVolver;
    private JList<Tarea> listaTareas;
    private DefaultListModel<Tarea> listModelTareas;

    public ExploradorTareasVista(List<Prioridad> prioridades, List<Proyecto> proyectos) {
        setTitle("Editar Tarea");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        comboFiltroProyecto = new JComboBox<>();
        Proyecto todosLosProyectos = new Proyecto();
        todosLosProyectos.setIdProyecto(0);
        todosLosProyectos.setNombreProyecto("Todos los Proyectos");
        comboFiltroProyecto.addItem(todosLosProyectos);
        proyectos.forEach(comboFiltroProyecto::addItem);
        comboFiltroProyecto.setFont(ConstantesUI.FUENTE_NORMAL);

        comboFiltroPrioridad = new JComboBox<>();
        Prioridad todasLasPrioridades = new Prioridad();
        todasLasPrioridades.setIdPrioridad(0);
        todasLasPrioridades.setNombrePrioridad("Todas las Prioridades");
        comboFiltroPrioridad.addItem(todasLasPrioridades);
        prioridades.forEach(comboFiltroPrioridad::addItem);
        comboFiltroPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);

        txtBusqueda = new JTextField(20);
        txtBusqueda.setFont(ConstantesUI.FUENTE_NORMAL);
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

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
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);
        panelSuperior.add(panelVolver, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel de Contenido
        listModelTareas = new DefaultListModel<>();
        listaTareas = new JList<>(listModelTareas);
        listaTareas.setFont(ConstantesUI.FUENTE_NORMAL);
        listaTareas.setCellRenderer(new TareaListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(listaTareas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);

        JLabel footer = new JLabel("Doble clic en una tarea para editarla", SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(footer, BorderLayout.SOUTH);
    }

    // Getters
    public JComboBox<Proyecto> getComboFiltroProyecto() { return comboFiltroProyecto; }
    public JComboBox<Prioridad> getComboFiltroPrioridad() { return comboFiltroPrioridad; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnVolver() { return btnVolver; }
    public JList<Tarea> getListaTareas() { return listaTareas; }
    public DefaultListModel<Tarea> getListModelTareas() { return listModelTareas; }

    // Clase interna para renderizar las tareas en la lista de forma más atractiva
    private static class TareaListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Tarea tarea) {
                String texto = String.format(
                        "<html><body style='width: 300px'><b>%s</b><br>" +
                                "<font color='gray'>Prioridad: %s | Estado: %s</font>" +
                                "</body></html>",
                        tarea.getNombreTarea(), tarea.getNombrePrioridad(), tarea.getNombreEstado());
                label.setText(texto);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            }
            return label;
        }
    }
}