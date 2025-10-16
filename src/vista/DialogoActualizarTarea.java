package vista;

import modelo.Comentario;
import modelo.Estado;
import modelo.Tarea;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class DialogoActualizarTarea extends JDialog {
    private JComboBox<Estado> comboEstado;
    private JList<Comentario> listaComentarios;
    private DefaultListModel<Comentario> listModelComentarios;
    private JTextArea areaNuevoComentario;
    private JButton btnPublicarComentario;
    private JButton btnGuardar, btnCerrar;
    private Tarea tarea;
    private boolean guardado = false;
    private DefaultListModel<Usuario> listModelUsuarios; // <-- NUEVO

    public DialogoActualizarTarea(Frame owner, Tarea tarea, List<Estado> estados) {
        super(owner, "Actualizar Tarea: " + tarea.getNombreTarea(), true);
        this.tarea = tarea;
        setSize(600, 700); // Aumentamos la altura
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Superior: Información y Cambio de Estado ---
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Información de la Tarea"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        gbc.gridx = 0; gbc.gridy = 0; panelSuperior.add(new JLabel("<html><b>Tarea:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelSuperior.add(new JLabel(tarea.getNombreTarea()), gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelSuperior.add(new JLabel("<html><b>Prioridad:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelSuperior.add(new JLabel(tarea.getNombrePrioridad()), gbc);

        // --- CAMPOS AÑADIDOS ---
        gbc.gridx = 0; gbc.gridy = 2; panelSuperior.add(new JLabel("<html><b>Fecha Inicio:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelSuperior.add(new JLabel(tarea.getFechaInicio() != null ? sdf.format(tarea.getFechaInicio()) : "N/A"), gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelSuperior.add(new JLabel("<html><b>Fecha Fin Estimada:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelSuperior.add(new JLabel(tarea.getFechaFinalEstimada() != null ? sdf.format(tarea.getFechaFinalEstimada()) : "N/A"), gbc);
        // -----------------------

        gbc.gridx = 0; gbc.gridy = 4; panelSuperior.add(new JLabel("<html><b>Cambiar Estado:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        comboEstado = new JComboBox<>(new Vector<>(estados));
        estados.stream().filter(e -> e.getIdEstado() == tarea.getIdEstado()).findFirst().ifPresent(comboEstado::setSelectedItem);
        panelSuperior.add(comboEstado, gbc);

        // --- LISTA DE USUARIOS AÑADIDA ---
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.NORTHWEST; panelSuperior.add(new JLabel("<html><b>Usuarios Designados:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        listModelUsuarios = new DefaultListModel<>();
        JList<Usuario> listaUsuarios = new JList<>(listModelUsuarios);
        panelSuperior.add(new JScrollPane(listaUsuarios), gbc);
        // -----------------------------

        // --- Panel de Comentarios ---
        JPanel panelComentarios = new JPanel(new BorderLayout(5, 5));
        panelComentarios.setBorder(BorderFactory.createTitledBorder("Comentarios"));

        listModelComentarios = new DefaultListModel<>();
        listaComentarios = new JList<>(listModelComentarios);
        listaComentarios.setCellRenderer(new ComentarioListCellRenderer());
        panelComentarios.add(new JScrollPane(listaComentarios), BorderLayout.CENTER);

        JPanel panelNuevoComentario = new JPanel(new BorderLayout(5, 5));
        areaNuevoComentario = new JTextArea(3, 0);
        btnPublicarComentario = new JButton("Publicar");
        panelNuevoComentario.add(new JScrollPane(areaNuevoComentario), BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicarComentario, BorderLayout.EAST);
        panelComentarios.add(panelNuevoComentario, BorderLayout.SOUTH);

        // --- Panel de Botones Inferior ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar Cambios");
        btnCerrar = new JButton("Cerrar");
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnCerrar);

        // Listeners
        btnGuardar.addActionListener(e -> guardar());
        btnCerrar.addActionListener(e -> setVisible(false));

        add(panelSuperior, BorderLayout.NORTH);
        add(panelComentarios, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    // Getters para el controlador
    public JComboBox<Estado> getComboEstado() { return comboEstado; }
    public DefaultListModel<Comentario> getListModelComentarios() { return listModelComentarios; }
    public DefaultListModel<Usuario> getListModelUsuarios() { return listModelUsuarios; } // <-- NUEVO
    public JTextArea getAreaNuevoComentario() { return areaNuevoComentario; }
    public JButton getBtnPublicarComentario() { return btnPublicarComentario; }
    public Tarea getTarea() { return tarea; }
    public boolean isGuardado() { return guardado; }

    private void guardar() {
        this.guardado = true;
        setVisible(false);
    }

    private static class ComentarioListCellRenderer extends DefaultListCellRenderer {
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Comentario) {
                Comentario c = (Comentario) value;
                label.setText("<html><body style='width: 350px'><b>" + c.getNombreUsuarioComentario() + "</b> (" + sdf.format(c.getFechaCreacion()) + "):<br>" + c.getContenido() + "</body></html>");
            }
            return label;
        }
    }
}