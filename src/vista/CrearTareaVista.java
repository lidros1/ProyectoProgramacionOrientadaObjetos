// Archivo: src/vista/CrearTareaVista.java
package vista;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class CrearTareaVista extends JFrame {
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> listModelProyectos;
    private JPanel panelFormulario;
    private JTextField txtNombreTarea;
    private JComboBox<Prioridad> comboPrioridad;
    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFin;
    private JTextArea areaComentario;
    private JList<Usuario> listaUsuariosDisponibles;
    private JList<Usuario> listaUsuariosAsignados;
    private DefaultListModel<Usuario> modelDisponibles;
    private DefaultListModel<Usuario> modelAsignados;
    private JButton btnCrear, btnVolver;

    public CrearTareaVista(List<Prioridad> prioridades, List<Usuario> usuarios) {
        setTitle("Crear Nueva Tarea");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel Izquierdo: Selección de Proyecto
        JPanel panelBusquedaProyecto = new JPanel(new BorderLayout(5, 5));
        panelBusquedaProyecto.setBorder(ConstantesUI.BORDE_TITULO("1. Seleccionar Proyecto"));
        panelBusquedaProyecto.setOpaque(false);
        txtBuscarProyecto = new JTextField();
        txtBuscarProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        listModelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(listModelProyectos);
        listaProyectos.setFont(ConstantesUI.FUENTE_NORMAL);
        panelBusquedaProyecto.add(txtBuscarProyecto, BorderLayout.NORTH);
        panelBusquedaProyecto.add(new JScrollPane(listaProyectos), BorderLayout.CENTER);

        // Panel Derecho: Formulario
        panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setBorder(ConstantesUI.BORDE_TITULO("2. Detalles y Asignación"));
        panelFormulario.setOpaque(false);

        // Subpanel para detalles
        JPanel panelDetalles = new JPanel(new GridBagLayout());
        panelDetalles.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelDetalles.add(new JLabel("Nombre Tarea:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; txtNombreTarea = new JTextField(); txtNombreTarea.setFont(ConstantesUI.FUENTE_NORMAL); panelDetalles.add(txtNombreTarea, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDetalles.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades)); comboPrioridad.setFont(ConstantesUI.FUENTE_NORMAL); panelDetalles.add(comboPrioridad, gbc);

        Properties p = new Properties();
        p.put("text.today", "Hoy"); p.put("text.month", "Mes"); p.put("text.year", "Año");
        gbc.gridx = 0; gbc.gridy = 2; panelDetalles.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelDetalles.add(datePickerInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDetalles.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; datePickerFin = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelDetalles.add(datePickerFin, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST; panelDetalles.add(new JLabel("Comentario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.4;
        areaComentario = new JTextArea(4, 0);
        areaComentario.setFont(ConstantesUI.FUENTE_NORMAL);
        panelDetalles.add(new JScrollPane(areaComentario), gbc);

        // Panel de Asignación
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weighty = 0.6; gbc.fill = GridBagConstraints.BOTH;
        JPanel panelAsignacion = new JPanel(new GridLayout(1, 2, 10, 10));
        panelAsignacion.setOpaque(false);
        panelAsignacion.setBorder(ConstantesUI.BORDE_TITULO("Asignar Usuarios (Doble Clic)"));
        modelDisponibles = new DefaultListModel<>();
        usuarios.forEach(modelDisponibles::addElement);
        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        listaUsuariosDisponibles.setFont(ConstantesUI.FUENTE_NORMAL);
        modelAsignados = new DefaultListModel<>();
        listaUsuariosAsignados = new JList<>(modelAsignados);
        listaUsuariosAsignados.setFont(ConstantesUI.FUENTE_NORMAL);
        panelAsignacion.add(new JScrollPane(listaUsuariosDisponibles));
        panelAsignacion.add(new JScrollPane(listaUsuariosAsignados));
        panelDetalles.add(panelAsignacion, gbc);

        panelFormulario.add(panelDetalles, BorderLayout.CENTER);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusquedaProyecto, panelFormulario);
        splitPane.setDividerLocation(300);
        splitPane.setOpaque(false);
        add(splitPane, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotonesAccion.setOpaque(false);
        btnCrear = new JButton("Crear Tarea");
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrear);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelBotonesAccion.add(btnVolver);
        panelBotonesAccion.add(btnCrear);
        add(panelBotonesAccion, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtBuscarProyecto() { return txtBuscarProyecto; }
    public JList<Proyecto> getListaProyectos() { return listaProyectos; }
    public DefaultListModel<Proyecto> getListModelProyectos() { return listModelProyectos; }
    public JPanel getPanelFormulario() { return panelFormulario; }
    public JTextField getTxtNombreTarea() { return txtNombreTarea; }
    public JComboBox<Prioridad> getComboPrioridad() { return comboPrioridad; }
    public JDatePickerImpl getDatePickerInicio() { return datePickerInicio; }
    public JDatePickerImpl getDatePickerFin() { return datePickerFin; }
    public JTextArea getAreaComentario() { return areaComentario; }
    public JList<Usuario> getListaUsuariosDisponibles() { return listaUsuariosDisponibles; }
    public JList<Usuario> getListaUsuariosAsignados() { return listaUsuariosAsignados; }
    public DefaultListModel<Usuario> getModelDisponibles() { return modelDisponibles; }
    public DefaultListModel<Usuario> getModelAsignados() { return modelAsignados; }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnVolver() { return btnVolver; }
}