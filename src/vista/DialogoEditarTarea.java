// Archivo: src/vista/DialogoEditarTarea.java
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
import java.util.ArrayList;
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

    public DialogoEditarTarea(Window owner, Tarea tarea, List<Prioridad> prioridades, List<Estado> estados, List<Usuario> todosLosUsuarios) {
        super(owner, "Editar Tarea", ModalityType.APPLICATION_MODAL);
        this.tarea = tarea;

        // --- SOLUCIÓN: Tamaño más grande para el diálogo ---
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);

        // Panel de datos básicos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(ConstantesUI.BORDE_TITULO("Datos de la Tarea"));
        panelDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; txtNombre = new JTextField(tarea.getNombreTarea()); txtNombre.setFont(ConstantesUI.FUENTE_NORMAL); panelDatos.add(txtNombre, gbc);

        Properties p = new Properties();
        p.put("text.today", "Hoy"); p.put("text.month", "Mes"); p.put("text.year", "Año");

        gbc.gridx = 0; gbc.gridy = 1; panelDatos.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; datePickerInicio = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(tarea.getFechaInicio()), p), new DialogoEditarProyecto.DateLabelFormatter()); panelDatos.add(datePickerInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDatos.add(new JLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; datePickerFin = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(tarea.getFechaFinalEstimada()), p), new DialogoEditarProyecto.DateLabelFormatter()); panelDatos.add(datePickerFin, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; comboPrioridad = new JComboBox<>(new Vector<>(prioridades)); comboPrioridad.setFont(ConstantesUI.FUENTE_NORMAL);
        prioridades.stream().filter(pr -> pr.getIdPrioridad() == tarea.getIdPrioridad()).findFirst().ifPresent(comboPrioridad::setSelectedItem);
        panelDatos.add(comboPrioridad, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; comboEstado = new JComboBox<>(new Vector<>(estados)); comboEstado.setFont(ConstantesUI.FUENTE_NORMAL);
        estados.stream().filter(es -> es.getIdEstado() == tarea.getIdEstado()).findFirst().ifPresent(comboEstado::setSelectedItem);
        panelDatos.add(comboEstado, gbc);

        // Panel de asignación de usuarios
        JPanel panelUsuarios = new JPanel(new GridLayout(1, 2, 10, 10));
        panelUsuarios.setBorder(ConstantesUI.BORDE_TITULO("Asignar Usuarios (Doble Clic)"));
        panelUsuarios.setOpaque(false);

        DefaultListModel<Usuario> modelDisponibles = new DefaultListModel<>();
        DefaultListModel<Usuario> modelAsignados = new DefaultListModel<>();

        List<Integer> idsAsignados = tarea.getUsuariosDesignados().stream().map(Usuario::getIdUsuario).collect(Collectors.toList());
        todosLosUsuarios.forEach(u -> {
            if (idsAsignados.contains(u.getIdUsuario())) modelAsignados.addElement(u);
            else modelDisponibles.addElement(u);
        });

        listaUsuariosDisponibles = new JList<>(modelDisponibles);
        listaUsuariosDisponibles.setFont(ConstantesUI.FUENTE_NORMAL);
        listaUsuariosAsignados = new JList<>(modelAsignados);
        listaUsuariosAsignados.setFont(ConstantesUI.FUENTE_NORMAL);

        panelUsuarios.add(new JScrollPane(listaUsuariosDisponibles));
        panelUsuarios.add(new JScrollPane(listaUsuariosAsignados));

        // Listeners para mover usuarios
        listaUsuariosDisponibles.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) moverUsuario(listaUsuariosDisponibles, listaUsuariosAsignados);
            }
        });
        listaUsuariosAsignados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) moverUsuario(listaUsuariosAsignados, listaUsuariosDisponibles);
            }
        });

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

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelCentral.add(panelDatos, BorderLayout.NORTH);
        panelCentral.add(panelUsuarios, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
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

        if (comboPrioridad.getSelectedItem() instanceof Prioridad p) tarea.setIdPrioridad(p.getIdPrioridad());
        if (comboEstado.getSelectedItem() instanceof Estado est) tarea.setIdEstado(est.getIdEstado());

        DefaultListModel<Usuario> modelAsignados = (DefaultListModel<Usuario>) listaUsuariosAsignados.getModel();
        List<Usuario> nuevosAsignados = new ArrayList<>();
        for (int i = 0; i < modelAsignados.getSize(); i++) nuevosAsignados.add(modelAsignados.getElementAt(i));
        tarea.setUsuariosDesignados(nuevosAsignados);

        this.guardado = true;
        dispose();
    }

    public boolean isGuardado() { return guardado; }
    public Tarea getTareaActualizada() { return tarea; }
}