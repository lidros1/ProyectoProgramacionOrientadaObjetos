// Archivo: src/vista/TareaDetalleVista.java
package vista;

import modelo.Comentario;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class TareaDetalleVista extends JFrame {
    private JLabel lblNombreTarea, lblPorcentajeAvance, lblFechaInicio, lblFechaFinEstimada,
            lblEstado, lblPrioridad;
    private DefaultListModel<Comentario> listModelComentarios;
    private DefaultListModel<Usuario> listModelUsuariosTarea;
    private JTextArea areaNuevoComentario;
    private JButton btnPublicarComentario;
    private JButton btnVolverATareas;

    public TareaDetalleVista() {
        setTitle("Detalles de la Tarea");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // Panel de Detalles
        JPanel panelDetallesTarea = new JPanel(new GridBagLayout());
        panelDetallesTarea.setBorder(ConstantesUI.BORDE_TITULO("Información de la Tarea"));
        panelDetallesTarea.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; panelDetallesTarea.add(new JLabel("<html><b>Tarea:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; lblNombreTarea = new JLabel(); lblNombreTarea.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblNombreTarea, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDetallesTarea.add(new JLabel("<html><b>Avance:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; lblPorcentajeAvance = new JLabel(); lblPorcentajeAvance.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblPorcentajeAvance, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDetallesTarea.add(new JLabel("<html><b>Inicio:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; lblFechaInicio = new JLabel(); lblFechaInicio.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDetallesTarea.add(new JLabel("<html><b>Fin Estimado:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; lblFechaFinEstimada = new JLabel(); lblFechaFinEstimada.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblFechaFinEstimada, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelDetallesTarea.add(new JLabel("<html><b>Estado:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; lblEstado = new JLabel(); lblEstado.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelDetallesTarea.add(new JLabel("<html><b>Prioridad:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; lblPrioridad = new JLabel(); lblPrioridad.setFont(ConstantesUI.FUENTE_NORMAL); panelDetallesTarea.add(lblPrioridad, gbc);

        // Panel Izquierdo: Detalles y Comentarios
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 10));
        panelIzquierdo.setOpaque(false);

        JPanel panelComentarios = new JPanel(new BorderLayout(5, 5));
        panelComentarios.setBorder(ConstantesUI.BORDE_TITULO("Comentarios"));
        panelComentarios.setOpaque(false);
        listModelComentarios = new DefaultListModel<>();
        JList<Comentario> listaComentarios = new JList<>(listModelComentarios);

        // --- CORRECCIÓN: Usar el nuevo nombre de clase ---
        listaComentarios.setCellRenderer(new RenderizadorComentario());

        panelComentarios.add(new JScrollPane(listaComentarios), BorderLayout.CENTER);

        JPanel panelNuevoComentario = new JPanel(new BorderLayout(5, 5));
        panelNuevoComentario.setOpaque(false);
        areaNuevoComentario = new JTextArea(3, 0);
        areaNuevoComentario.setLineWrap(true);
        areaNuevoComentario.setWrapStyleWord(true);
        btnPublicarComentario = new JButton("Publicar");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnPublicarComentario);
        panelNuevoComentario.add(new JScrollPane(areaNuevoComentario), BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicarComentario, BorderLayout.EAST);
        panelComentarios.add(panelNuevoComentario, BorderLayout.SOUTH);

        panelIzquierdo.add(panelDetallesTarea, BorderLayout.NORTH);
        panelIzquierdo.add(panelComentarios, BorderLayout.CENTER);

        // Panel Derecho: Usuarios designados
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(ConstantesUI.BORDE_TITULO("Usuarios Designados"));
        panelDerecho.setOpaque(false);
        listModelUsuariosTarea = new DefaultListModel<>();
        JList<Usuario> listaUsuariosTarea = new JList<>(listModelUsuariosTarea);
        listaUsuariosTarea.setFont(ConstantesUI.FUENTE_NORMAL);
        panelDerecho.add(new JScrollPane(listaUsuariosTarea), BorderLayout.CENTER);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(0.6);
        splitPane.setResizeWeight(0.6);
        splitPane.setOpaque(false);
        add(splitPane, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setOpaque(false);
        btnVolverATareas = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolverATareas);
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
}