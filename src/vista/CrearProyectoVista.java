// Archivo: src/vista/CrearProyectoVista.java
package vista;

import modelo.Prioridad;
import javax.swing.*;
import java.awt.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.List;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(ConstantesUI.BORDE_TITULO("Datos del Nuevo Proyecto"));
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        JLabel lblNombre = new JLabel("Nombre del Proyecto:");
        lblNombre.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.9;
        txtNombreProyecto = new JTextField(25);
        txtNombreProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(txtNombreProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblDesc = new JLabel("Descripción (opcional):");
        lblDesc.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(lblDesc, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtDescripcionProyecto = new JTextArea(4, 25);
        txtDescripcionProyecto.setFont(ConstantesUI.FUENTE_NORMAL);
        txtDescripcionProyecto.setLineWrap(true);
        txtDescripcionProyecto.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcionProyecto);
        panelFormulario.add(scrollDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblInicio = new JLabel("Fecha de Inicio:");
        lblInicio.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(lblInicio, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        UtilDateModel modelInicio = new UtilDateModel();
        Properties pInicio = new Properties();
        pInicio.put("text.today", "Hoy");
        pInicio.put("text.month", "Mes");
        pInicio.put("text.year", "Año");
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modelInicio, pInicio);
        datePickerFechaInicio = new JDatePickerImpl(datePanelInicio, new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelFormulario.add(datePickerFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblFin = new JLabel("Fecha Final Estimada:");
        lblFin.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(lblFin, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        UtilDateModel modelFin = new UtilDateModel();
        Properties pFin = new Properties();
        pFin.put("text.today", "Hoy");
        pFin.put("text.month", "Mes");
        pFin.put("text.year", "Año");
        JDatePanelImpl datePanelFin = new JDatePanelImpl(modelFin, pFin);
        datePickerFechaFinalEstimada = new JDatePickerImpl(datePanelFin, new FormateadorFecha()); // <-- CAMBIO AQUÍ
        panelFormulario.add(datePickerFechaFinalEstimada, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblPrio = new JLabel("Prioridad:");
        lblPrio.setFont(ConstantesUI.FUENTE_NORMAL);
        panelFormulario.add(lblPrio, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        comboPrioridad = new JComboBox<>();
        comboPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        prioridades.forEach(comboPrioridad::addItem);
        panelFormulario.add(comboPrioridad, gbc);

        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setOpaque(false);
        GridBagConstraints gbcContenedor = new GridBagConstraints();
        gbcContenedor.weightx = 0.6;
        gbcContenedor.weighty = 1.0;
        gbcContenedor.fill = GridBagConstraints.HORIZONTAL;
        panelContenedor.add(panelFormulario, gbcContenedor);

        add(panelContenedor, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setOpaque(false);
        btnCrearProyecto = new JButton("Crear Proyecto");
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnCrearProyecto);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelBotones.add(btnVolver);
        panelBotones.add(btnCrearProyecto);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTextField getTxtNombreProyecto() { return txtNombreProyecto; }
    public JTextArea getTxtDescripcionProyecto() { return txtDescripcionProyecto; }
    public JDatePickerImpl getDatePickerFechaInicio() { return datePickerFechaInicio; }
    public JDatePickerImpl getDatePickerFechaFinalEstimada() { return datePickerFechaFinalEstimada; }
    public JComboBox<Prioridad> getComboPrioridad() { return comboPrioridad; }
    public JButton getBtnCrearProyecto() { return btnCrearProyecto; }
    public JButton getBtnVolver() { return btnVolver; }

    // --- SE ELIMINÓ LA CLASE INTERNA DateLabelFormatter ---
}