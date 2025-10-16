package vista;

import modelo.Comentario;
import modelo.Tarea;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TareaDetalleVista extends JFrame {
    private JLabel lblNombreTarea, lblPorcentajeAvance, lblFechaInicio, lblFechaFinEstimada,
            lblEstado, lblPrioridad;
    private JList<Comentario> listaComentarios;
    private DefaultListModel<Comentario> listModelComentarios;
    private JList<Usuario> listaUsuariosTarea;
    private DefaultListModel<Usuario> listModelUsuariosTarea;
    private JTextArea areaNuevoComentario; // <-- AÑADIDO
    private JButton btnPublicarComentario;  // <-- AÑADIDO
    private JButton btnVolverATareas;

    public TareaDetalleVista() {
        setTitle("Detalles de la Tarea");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Detalles de la Tarea (Superior) ---
        JPanel panelDetallesTarea = new JPanel(new GridBagLayout());
        panelDetallesTarea.setBorder(BorderFactory.createTitledBorder("Información de la Tarea"));
        panelDetallesTarea.setPreferredSize(new Dimension(panelDetallesTarea.getPreferredSize().width, 350));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDetallesTarea.add(new JLabel("<html><b>Tarea:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; lblNombreTarea = new JLabel(); panelDetallesTarea.add(lblNombreTarea, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDetallesTarea.add(new JLabel("<html><b>Avance:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; lblPorcentajeAvance = new JLabel(); panelDetallesTarea.add(lblPorcentajeAvance, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDetallesTarea.add(new JLabel("<html><b>Inicio:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; lblFechaInicio = new JLabel(); panelDetallesTarea.add(lblFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDetallesTarea.add(new JLabel("<html><b>Fin Estimado:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; lblFechaFinEstimada = new JLabel(); panelDetallesTarea.add(lblFechaFinEstimada, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelDetallesTarea.add(new JLabel("<html><b>Estado:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; lblEstado = new JLabel(); panelDetallesTarea.add(lblEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelDetallesTarea.add(new JLabel("<html><b>Prioridad:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; lblPrioridad = new JLabel(); panelDetallesTarea.add(lblPrioridad, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; panelDetallesTarea.add(Box.createVerticalStrut(10), gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; panelDetallesTarea.add(new JLabel("<html><b>Usuarios Designados:</b></html>"), gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.weighty = 0.5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        listModelUsuariosTarea = new DefaultListModel<>();
        listaUsuariosTarea = new JList<>(listModelUsuariosTarea);
        panelDetallesTarea.add(new JScrollPane(listaUsuariosTarea), gbc);

        add(panelDetallesTarea, BorderLayout.NORTH);

        // --- Panel de Comentarios (Centro) ---
        JPanel panelComentarios = new JPanel(new BorderLayout(5, 5));
        panelComentarios.setBorder(BorderFactory.createTitledBorder("Comentarios"));

        listModelComentarios = new DefaultListModel<>();
        listaComentarios = new JList<>(listModelComentarios);
        listaComentarios.setCellRenderer(new ComentarioListCellRenderer());
        panelComentarios.add(new JScrollPane(listaComentarios), BorderLayout.CENTER);

        // --- Panel para añadir un nuevo comentario ---
        JPanel panelNuevoComentario = new JPanel(new BorderLayout(5, 5));
        panelNuevoComentario.setBorder(BorderFactory.createTitledBorder("Añadir Comentario"));
        areaNuevoComentario = new JTextArea(3, 0);
        areaNuevoComentario.setLineWrap(true);
        areaNuevoComentario.setWrapStyleWord(true);
        btnPublicarComentario = new JButton("Publicar");
        panelNuevoComentario.add(new JScrollPane(areaNuevoComentario), BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicarComentario, BorderLayout.EAST);

        panelComentarios.add(panelNuevoComentario, BorderLayout.SOUTH);

        add(panelComentarios, BorderLayout.CENTER);

        // --- Panel de Botones (Inferior) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolverATareas = new JButton("Volver a Tareas");
        panelBotones.add(btnVolverATareas);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JLabel getLblNombreTarea() { return lblNombreTarea; }
    public JLabel getLblPorcentajeAvance() { return lblPorcentajeAvance; }
    public JLabel getLblFechaInicio() { return lblFechaInicio; }
    public JLabel getLblFechaFinEstimada() { return lblFechaFinEstimada; }
    public JLabel getLblEstado() { return lblEstado; }
    public JLabel getLblPrioridad() { return lblPrioridad; }
    public DefaultListModel<Comentario> getListModelComentarios() { return listModelComentarios; }
    public DefaultListModel<Usuario> getListModelUsuariosTarea() { return listModelUsuariosTarea; }
    public JTextArea getAreaNuevoComentario() { return areaNuevoComentario; }
    public JButton getBtnPublicarComentario() { return btnPublicarComentario; }
    public JButton getBtnVolverATareas() { return btnVolverATareas; }

    private static class ComentarioListCellRenderer extends DefaultListCellRenderer {
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Comentario) {
                Comentario comentario = (Comentario) value;
                label.setText("<html><b>" + comentario.getNombreUsuarioComentario() + "</b> (" + sdf.format(comentario.getFechaCreacion()) + "): " + comentario.getContenido() + "</html>");
            }
            return label;
        }
    }
}