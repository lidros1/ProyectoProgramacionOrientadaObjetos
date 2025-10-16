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
    // --- Panel de Selección de Proyecto ---
    private JTextField txtBuscarProyecto;
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> listModelProyectos;

    // --- Panel de Formulario ---
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
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Selección de Proyecto (Izquierda) ---
        JPanel panelBusquedaProyecto = new JPanel(new BorderLayout(5, 5));
        panelBusquedaProyecto.setBorder(BorderFactory.createTitledBorder("1. Seleccionar Proyecto"));
        txtBuscarProyecto = new JTextField();
        listModelProyectos = new DefaultListModel<>();
        listaProyectos = new JList<>(listModelProyectos);
        panelBusquedaProyecto.add(txtBuscarProyecto, BorderLayout.NORTH);
        panelBusquedaProyecto.add(new JScrollPane(listaProyectos), BorderLayout.CENTER);

        // --- Panel Central: Formulario (Derecha) ---
        panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("2. Detalles de la Tarea y Asignación"));

        // Subpanel para detalles de la tarea
        JPanel panelDetalles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelDetalles.add(new JLabel("Nombre Tarea:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; txtNombreTarea = new JTextField(); panelDetalles.add(txtNombreTarea, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDetalles.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades)); panelDetalles.add(comboPrioridad, gbc);

        Properties p = new Properties();
        p.put("text.today", "Today"); p.put("text.month", "Month"); p.put("text.year", "Year");
        gbc.gridx = 0; gbc.gridy = 2; panelDetalles.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DialogoEditarTarea.DateLabelFormatter()); panelDetalles.add(datePickerInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDetalles.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; datePickerFin = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DialogoEditarTarea.DateLabelFormatter()); panelDetalles.add(datePickerFin, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST; panelDetalles.add(new JLabel("Comentario (Opcional):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        areaComentario = new JTextArea(4, 0);
        panelDetalles.add(new JScrollPane(areaComentario), gbc);

        // --- MODIFICACIÓN: Se eliminan los botones >> y << y se ajusta el layout ---
        JPanel panelAsignacion = new JPanel(new GridLayout(1, 2, 10, 10));
        panelAsignacion.setBorder(BorderFactory.createTitledBorder("Asignar Usuarios"));

        modelDisponibles = new DefaultListModel<>();
        usuarios.forEach(modelDisponibles::addElement);
        listaUsuariosDisponibles = new JList<>(modelDisponibles);

        modelAsignados = new DefaultListModel<>();
        listaUsuariosAsignados = new JList<>(modelAsignados);

        JPanel panelDisponibles = new JPanel(new BorderLayout());
        panelDisponibles.add(new JLabel("Disponibles (Doble clic para agregar):"), BorderLayout.NORTH);
        panelDisponibles.add(new JScrollPane(listaUsuariosDisponibles), BorderLayout.CENTER);

        JPanel panelAsignados = new JPanel(new BorderLayout());
        panelAsignados.add(new JLabel("Asignados (Doble clic para quitar):"), BorderLayout.NORTH);
        panelAsignados.add(new JScrollPane(listaUsuariosAsignados), BorderLayout.CENTER);

        panelAsignacion.add(panelDisponibles);
        panelAsignacion.add(panelAsignados);
        // -----------------------------------------------------------------

        panelFormulario.add(panelDetalles, BorderLayout.CENTER);
        panelFormulario.add(panelAsignacion, BorderLayout.SOUTH);

        JPanel panelBotonesAccion = new JPanel(new BorderLayout());
        btnCrear = new JButton("Crear Tarea");
        btnVolver = new JButton("Volver");
        panelBotonesAccion.add(btnVolver, BorderLayout.WEST);
        panelBotonesAccion.add(btnCrear, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBusquedaProyecto, panelFormulario);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);
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