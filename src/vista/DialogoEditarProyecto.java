// Archivo: src/vista/DialogoEditarProyecto.java
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

    public DialogoEditarProyecto(Window owner, Proyecto proyecto, List<Prioridad> prioridades, List<Estado> estados) {
        super(owner, "Editar Proyecto", ModalityType.APPLICATION_MODAL);
        this.proyecto = proyecto;

        // --- SOLUCIÓN: Tamaño más grande para el diálogo ---
        setSize(550, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);

        // Panel de datos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(ConstantesUI.BORDE_TITULO("Datos del Proyecto"));
        panelDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 0: Nombre
        gbc.gridx=0; gbc.gridy=0; gbc.weightx = 0.2; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx = 0.8; txtNombre = new JTextField(proyecto.getNombreProyecto()); txtNombre.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtNombre, gbc);

        // Fila 1: Prioridad
        gbc.gridx=0; gbc.gridy=1; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx=1; gbc.gridy=1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades)); comboPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        for (Prioridad p : prioridades) {
            if (p.getNombrePrioridad().equals(proyecto.getNombrePrioridad())) {
                comboPrioridad.setSelectedItem(p);
                break;
            }
        }
        panelDatos.add(comboPrioridad, gbc);

        // Fila 2: Estado
        gbc.gridx=0; gbc.gridy=2; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; gbc.gridy=2; comboEstado = new JComboBox<>(new Vector<>(estados)); comboEstado.setFont(ConstantesUI.FUENTE_NORMAL);
        for (Estado e : estados) {
            if (e.getNombreEstado().equals(proyecto.getNombreEstado())) {
                comboEstado.setSelectedItem(e);
                break;
            }
        }
        panelDatos.add(comboEstado, gbc);

        // Campos de fecha
        Properties p = new Properties();
        p.put("text.today", "Hoy"); p.put("text.month", "Mes"); p.put("text.year", "Año");

        // Fila 3: Fecha Inicio
        gbc.gridx = 0; gbc.gridy = 3; panelDatos.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        UtilDateModel modelInicio = new UtilDateModel(proyecto.getFechaInicio());
        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelInicio, p), new DateLabelFormatter());
        panelDatos.add(datePickerInicio, gbc);

        // Fila 4: Fecha Fin Estimada
        gbc.gridx = 0; gbc.gridy = 4; panelDatos.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        UtilDateModel modelFin = new UtilDateModel(proyecto.getFechaFinalEstimada());
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(modelFin, p), new DateLabelFormatter());
        panelDatos.add(datePickerFin, gbc);

        // Panel de botones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelAcciones.setOpaque(false);
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGuardar);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnCancelar);
        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelCentral.add(panelDatos, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private void guardar() {
        proyecto.setNombreProyecto(txtNombre.getText());

        if (comboPrioridad.getSelectedItem() instanceof Prioridad p) {
            proyecto.setIdPrioridad(p.getIdPrioridad());
        }
        if (comboEstado.getSelectedItem() instanceof Estado est) {
            proyecto.setIdEstado(est.getIdEstado());
        }

        proyecto.setFechaInicio((Date) datePickerInicio.getModel().getValue());
        proyecto.setFechaFinalEstimada((Date) datePickerFin.getModel().getValue());

        this.guardado = true;
        dispose();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Proyecto getProyectoActualizado() {
        return proyecto;
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value instanceof Date) {
                return dateFormatter.format(value);
            } else if (value instanceof Calendar) {
                return dateFormatter.format(((Calendar) value).getTime());
            }
            return "";
        }
    }
}