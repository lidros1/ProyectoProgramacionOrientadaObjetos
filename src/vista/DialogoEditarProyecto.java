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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class DialogoEditarProyecto extends JDialog {
    private JTextField txtNombre;
    private JComboBox<Prioridad> comboPrioridad;
    private JComboBox<Estado> comboEstado;
    private JComboBox<String> comboActivo;
    private JDatePickerImpl datePickerInicio;
    private JDatePickerImpl datePickerFin;
    private JButton btnGuardar, btnCancelar;
    private Proyecto proyecto;
    private boolean guardado = false;

    public DialogoEditarProyecto(Window owner, Proyecto proyecto, List<Prioridad> prioridades, List<Estado> estados) {
        super(owner, "Editar Proyecto", ModalityType.APPLICATION_MODAL);
        this.proyecto = proyecto;

        setSize(550, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);

        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(ConstantesUI.BORDE_TITULO("Datos del Proyecto"));
        panelDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx=0; gbc.gridy=0; gbc.weightx = 0.3; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx = 0.7; txtNombre = new JTextField(proyecto.getNombreProyecto()); txtNombre.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtNombre, gbc);

        gbc.gridx=0; gbc.gridy=1; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx=1; gbc.gridy=1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades)); comboPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        prioridades.stream().filter(p -> p.getNombrePrioridad().equals(proyecto.getNombrePrioridad())).findFirst().ifPresent(comboPrioridad::setSelectedItem);
        panelDatos.add(comboPrioridad, gbc);

        gbc.gridx=0; gbc.gridy=2; panelDatos.add(new JLabel("Estado (Flujo):"), gbc);
        gbc.gridx=1; gbc.gridy=2; comboEstado = new JComboBox<>(new Vector<>(estados)); comboEstado.setFont(ConstantesUI.FUENTE_NORMAL);
        estados.stream().filter(e -> e.getNombreEstado().equals(proyecto.getNombreEstado())).findFirst().ifPresent(comboEstado::setSelectedItem);
        panelDatos.add(comboEstado, gbc);

        Properties p = new Properties();
        p.put("text.today", "Hoy"); p.put("text.month", "Mes"); p.put("text.year", "Año");

        gbc.gridx = 0; gbc.gridy = 3; panelDatos.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(proyecto.getFechaInicio()), p), new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelDatos.add(datePickerInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelDatos.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        datePickerFin = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(proyecto.getFechaFinalEstimada()), p), new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelDatos.add(datePickerFin, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelDatos.add(new JLabel("Estado (Registro):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        comboActivo = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        comboActivo.setFont(ConstantesUI.FUENTE_NORMAL);
        comboActivo.setSelectedItem(proyecto.getEstado());
        panelDatos.add(comboActivo, gbc);

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
        if (comboPrioridad.getSelectedItem() instanceof Prioridad p) proyecto.setIdPrioridad(p.getIdPrioridad());
        if (comboEstado.getSelectedItem() instanceof Estado est) proyecto.setIdEstado(est.getIdEstado());
        proyecto.setFechaInicio((Date) datePickerInicio.getModel().getValue());
        proyecto.setFechaFinalEstimada((Date) datePickerFin.getModel().getValue());
        if (comboActivo.getSelectedItem() != null) {
            proyecto.setEstado((String) comboActivo.getSelectedItem());
        }
        this.guardado = true;
        dispose();
    }

    public boolean isGuardado() { return guardado; }
    public Proyecto getProyectoActualizado() { return proyecto; }

    // --- SE ELIMINÓ LA CLASE INTERNA DateLabelFormatter ---
}