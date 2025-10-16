package vista;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Tarea;
import modelo.Usuario;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

public class DialogoEditarTarea extends JDialog {
    private JTextField txtNombre;
    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFin;
    private JComboBox<Prioridad> comboPrioridad;
    private JComboBox<Estado> comboEstado;
    private JList<Usuario> listaUsuariosDisponibles;
    private JList<Usuario> listaUsuariosAsignados;
    private JButton btnGuardar, btnCancelar;
    private Tarea tarea;
    private boolean guardado = false;

    public DialogoEditarTarea(Frame owner, Tarea tarea, List<Prioridad> prioridades, List<Estado> estados, List<Usuario> todosLosUsuarios) {
        super(owner, "Editar Tarea", true);
        this.tarea = tarea;
        setSize(700, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel de datos básicos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos de la Tarea"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtNombre = new JTextField(tarea.getNombreTarea()); panelDatos.add(txtNombre, gbc);

        UtilDateModel modelInicio = new UtilDateModel(tarea.getFechaInicio());
        UtilDateModel modelFin = new UtilDateModel(tarea.getFechaFinalEstimada());
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelInicio, p), new DateLabelFormatter()); panelDatos.add(datePickerInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDatos.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFin, p), new DateLabelFormatter()); panelDatos.add(datePickerFin, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; comboPrioridad = new JComboBox<>(new Vector<>(prioridades));
        prioridades.stream().filter(pr -> pr.getIdPrioridad() == tarea.getIdPrioridad()).findFirst().ifPresent(comboPrioridad::setSelectedItem);
        panelDatos.add(comboPrioridad, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; comboEstado = new JComboBox<>(new Vector<>(estados));
        estados.stream().filter(es -> es.getIdEstado() == tarea.getIdEstado()).findFirst().ifPresent(comboEstado::setSelectedItem);
        panelDatos.add(comboEstado, gbc);

        // Panel de asignación de usuarios
        JPanel panelUsuarios = new JPanel(new GridLayout(1, 2, 10, 10));
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Asignar Usuarios"));

        DefaultListModel<Usuario> modelDisponibles = new DefaultListModel<>();
        DefaultListModel<Usuario> modelAsignados = new DefaultListModel<>();

        List<Integer> idsAsignados = tarea.getUsuariosDesignados().stream().map(Usuario::getIdUsuario).collect(Collectors.toList());
        for (Usuario u : todosLosUsuarios) {
            if (idsAsignados.contains(u.getIdUsuario())) {
                modelAsignados.addElement(u);
            } else {
                modelDisponibles.addElement(u);
            }
        }

        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        listaUsuariosAsignados = new JList<>(modelAsignados);

        JPanel panelListaDisponibles = new JPanel(new BorderLayout());
        panelListaDisponibles.add(new JLabel("Disponibles (Doble clic para agregar):"), BorderLayout.NORTH);
        panelListaDisponibles.add(new JScrollPane(listaUsuariosDisponibles), BorderLayout.CENTER);

        JPanel panelListaAsignados = new JPanel(new BorderLayout());
        panelListaAsignados.add(new JLabel("Asignados (Doble clic para quitar):"), BorderLayout.NORTH);
        panelListaAsignados.add(new JScrollPane(listaUsuariosAsignados), BorderLayout.CENTER);

        panelUsuarios.add(panelListaDisponibles);
        panelUsuarios.add(panelListaAsignados);

        listaUsuariosDisponibles.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    moverUsuario(listaUsuariosDisponibles, listaUsuariosAsignados);
                }
            }
        });
        listaUsuariosAsignados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    moverUsuario(listaUsuariosAsignados, listaUsuariosDisponibles);
                }
            }
        });

        // Panel de botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> setVisible(false));

        add(panelDatos, BorderLayout.NORTH);
        add(panelUsuarios, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private void moverUsuario(JList<Usuario> origen, JList<Usuario> destino) {
        Usuario seleccionado = origen.getSelectedValue();
        if (seleccionado == null) return;
        ((DefaultListModel<Usuario>) origen.getModel()).removeElement(seleccionado);
        ((DefaultListModel<Usuario>) destino.getModel()).addElement(seleccionado);
    }

    private void guardar() {
        tarea.setNombreTarea(txtNombre.getText());
        tarea.setFechaInicio((Date) datePickerInicio.getModel().getValue());
        tarea.setFechaFinalEstimada((Date) datePickerFin.getModel().getValue());

        Prioridad p = (Prioridad) comboPrioridad.getSelectedItem();
        if (p != null) tarea.setIdPrioridad(p.getIdPrioridad());

        Estado est = (Estado) comboEstado.getSelectedItem();
        if (est != null) tarea.setIdEstado(est.getIdEstado());

        DefaultListModel<Usuario> modelAsignados = (DefaultListModel<Usuario>) listaUsuariosAsignados.getModel();
        List<Usuario> nuevosAsignados = new ArrayList<>();
        for (int i = 0; i < modelAsignados.getSize(); i++) {
            nuevosAsignados.add(modelAsignados.getElementAt(i));
        }
        tarea.setUsuariosDesignados(nuevosAsignados);

        this.guardado = true;
        setVisible(false);
    }

    public boolean isGuardado() { return guardado; }
    public Tarea getTareaActualizada() { return tarea; }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                if (value instanceof Calendar) {
                    return dateFormatter.format(((Calendar) value).getTime());
                }
            }
            return "";
        }
    }
}