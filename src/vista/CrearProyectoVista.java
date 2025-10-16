package vista;

import modelo.Prioridad;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar; // <-- IMPORT AÑADIDO
import java.util.List;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;

public class CrearProyectoVista extends JFrame {
    private JTextField txtNombreProyecto;
    private JTextArea txtDescripcionProyecto;
    private JDatePickerImpl datePickerFechaInicio;
    private JDatePickerImpl datePickerFechaFinalEstimada;
    private JComboBox<Prioridad> comboPrioridad;
    private JButton btnCrearProyecto;
    private JButton btnVolver;

    public CrearProyectoVista(List<Prioridad> prioridades) {
        setTitle("Crear Nuevo Proyecto");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Nombre del Proyecto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtNombreProyecto = new JTextField(25); panelFormulario.add(txtNombreProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Descripción (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtDescripcionProyecto = new JTextArea(4, 25);
        txtDescripcionProyecto.setLineWrap(true);
        txtDescripcionProyecto.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcionProyecto);
        panelFormulario.add(scrollDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Fecha de Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        UtilDateModel modelInicio = new UtilDateModel();
        Properties pInicio = new Properties();
        pInicio.put("text.today", "Today");
        pInicio.put("text.month", "Month");
        pInicio.put("text.year", "Year");
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modelInicio, pInicio);
        datePickerFechaInicio = new JDatePickerImpl(datePanelInicio, new DateLabelFormatter());
        panelFormulario.add(datePickerFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Fecha Final Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        UtilDateModel modelFin = new UtilDateModel();
        Properties pFin = new Properties();
        pFin.put("text.today", "Today");
        pFin.put("text.month", "Month");
        pFin.put("text.year", "Year");
        JDatePanelImpl datePanelFin = new JDatePanelImpl(modelFin, pFin);
        datePickerFechaFinalEstimada = new JDatePickerImpl(datePanelFin, new DateLabelFormatter());
        panelFormulario.add(datePickerFechaFinalEstimada, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; comboPrioridad = new JComboBox<>();
        for (Prioridad p : prioridades) {
            comboPrioridad.addItem(p);
        }
        panelFormulario.add(comboPrioridad, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrearProyecto = new JButton("Crear Proyecto");
        btnVolver = new JButton("Volver");
        panelBotones.add(btnCrearProyecto);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTextField getTxtNombreProyecto() { return txtNombreProyecto; }
    public JTextArea getTxtDescripcionProyecto() { return txtDescripcionProyecto; }
    public JDatePickerImpl getDatePickerFechaInicio() { return datePickerFechaInicio; }
    public JDatePickerImpl getDatePickerFechaFinalEstimada() { return datePickerFechaFinalEstimada; }
    public JComboBox<Prioridad> getComboPrioridad() { return comboPrioridad; }
    public JButton getBtnCrearProyecto() { return btnCrearProyecto; }
    public JButton getBtnVolver() { return btnVolver; }

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
                // --- CORRECCIÓN AQUÍ ---
                // El valor que llega es de tipo Calendar, no Date.
                // Lo convertimos a Date usando .getTime() antes de formatearlo.
                if (value instanceof Calendar) {
                    return dateFormatter.format(((Calendar) value).getTime());
                }
            }
            return "";
        }
    }
}