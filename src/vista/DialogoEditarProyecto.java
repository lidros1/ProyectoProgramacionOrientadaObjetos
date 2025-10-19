package vista;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Proyecto;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class DialogoEditarProyecto extends JDialog {
    private JTextField txtNombre;
    private JComboBox<Prioridad> comboPrioridad;
    private JComboBox<Estado> comboEstado;
    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFin;
    private JButton btnGuardar, btnCancelar;
    private Proyecto proyecto;
    private boolean guardado = false;

    public DialogoEditarProyecto(JFrame owner, Proyecto proyecto, List<Prioridad> prioridades, List<Estado> estados) {
        super(owner, "Editar Proyecto", true);
        this.proyecto = proyecto;
        setSize(450, 350); // Aumentamos el tamaño para los nuevos campos
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel de datos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Proyecto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 0: Nombre
        gbc.gridx=0; gbc.gridy=0; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtNombre = new JTextField(proyecto.getNombreProyecto()); panelDatos.add(txtNombre, gbc);

        // Fila 1: Prioridad
        gbc.gridx=0; gbc.gridy=1; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx=1; gbc.gridy=1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades));
        for (Prioridad p : prioridades) {
            if (p.getNombrePrioridad().equals(proyecto.getNombrePrioridad())) {
                comboPrioridad.setSelectedItem(p);
                break;
            }
        }
        panelDatos.add(comboPrioridad, gbc);

        // Fila 2: Estado
        gbc.gridx=0; gbc.gridy=2; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; gbc.gridy=2; comboEstado = new JComboBox<>(new Vector<>(estados));
        for (Estado e : estados) {
            if (e.getNombreEstado().equals(proyecto.getNombreEstado())) {
                comboEstado.setSelectedItem(e);
                break;
            }
        }
        panelDatos.add(comboEstado, gbc);

        // --- NUEVOS CAMPOS DE FECHA ---
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        // Fila 3: Fecha Inicio
        UtilDateModel modelInicio = new UtilDateModel(proyecto.getFechaInicio());
        gbc.gridx = 0; gbc.gridy = 3; panelDatos.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelInicio, p), new DateLabelFormatter());
        panelDatos.add(datePickerInicio, gbc);

        // Fila 4: Fecha Fin Estimada
        UtilDateModel modelFin = new UtilDateModel(proyecto.getFechaFinalEstimada());
        gbc.gridx = 0; gbc.gridy = 4; panelDatos.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFin, p), new DateLabelFormatter());
        panelDatos.add(datePickerFin, gbc);
        // -----------------------------

        // Panel de botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> setVisible(false));

        add(panelDatos, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private void guardar() {
        // Recolectar todos los datos, incluyendo las fechas
        proyecto.setNombreProyecto(txtNombre.getText());

        Prioridad p = (Prioridad) comboPrioridad.getSelectedItem();
        if (p != null) proyecto.setIdPrioridad(p.getIdPrioridad());

        Estado est = (Estado) comboEstado.getSelectedItem();
        if (est != null) proyecto.setIdEstado(est.getIdEstado());

        proyecto.setFechaInicio((Date) datePickerInicio.getModel().getValue());
        proyecto.setFechaFinalEstimada((Date) datePickerFin.getModel().getValue());

        this.guardado = true;
        setVisible(false);
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Proyecto getProyectoActualizado() {
        return proyecto;
    }

    // Clase interna para formatear la fecha en el JDatePicker
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof Calendar) {
                    return dateFormatter.format(((Calendar) value).getTime());
                } else if (value instanceof Date) {
                    return dateFormatter.format(value);
                }
            }
            return "";
        }
    }
}