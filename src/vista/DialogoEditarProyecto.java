package vista;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class DialogoEditarProyecto extends JDialog {
    private JTextField txtNombre;
    private JComboBox<Prioridad> comboPrioridad;
    private JComboBox<Estado> comboEstado;
    private JButton btnGuardar, btnCancelar;
    private Proyecto proyecto;
    private boolean guardado = false;

    public DialogoEditarProyecto(JFrame owner, Proyecto proyecto, List<Prioridad> prioridades, List<Estado> estados) {
        super(owner, "Editar Proyecto", true);
        this.proyecto = proyecto;
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel de datos básicos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Proyecto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx=0; gbc.gridy=0; panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtNombre = new JTextField(proyecto.getNombreProyecto()); panelDatos.add(txtNombre, gbc);

        gbc.gridx=0; gbc.gridy=1; panelDatos.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx=1; gbc.gridy=1; comboPrioridad = new JComboBox<>(new Vector<>(prioridades));
        for (Prioridad p : prioridades) {
            if (p.getNombrePrioridad().equals(proyecto.getNombrePrioridad())) {
                comboPrioridad.setSelectedItem(p);
                break;
            }
        }
        panelDatos.add(comboPrioridad, gbc);

        gbc.gridx=0; gbc.gridy=2; panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; gbc.gridy=2; comboEstado = new JComboBox<>(new Vector<>(estados));
        for (Estado e : estados) {
            if (e.getNombreEstado().equals(proyecto.getNombreEstado())) {
                comboEstado.setSelectedItem(e);
                break;
            }
        }
        panelDatos.add(comboEstado, gbc);

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
        proyecto.setNombreProyecto(txtNombre.getText());
        Prioridad p = (Prioridad) comboPrioridad.getSelectedItem();
        if (p != null) proyecto.setIdPrioridad(p.getIdPrioridad());
        Estado est = (Estado) comboEstado.getSelectedItem();
        if (est != null) proyecto.setIdEstado(est.getIdEstado());

        this.guardado = true;
        setVisible(false);
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Proyecto getProyectoActualizado() {
        return proyecto;
    }
}