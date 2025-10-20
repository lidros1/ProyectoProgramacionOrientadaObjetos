// Archivo: src/vista/DialogoActualizarTarea.java
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
    private DefaultListModel<Usuario> listModelUsuarios;

    public DialogoActualizarTarea(Frame owner, Tarea tarea, List<Estado> estados) {
        super(owner, "Actualizar Tarea: " + tarea.getNombreTarea(), true);
        this.tarea = tarea;

        setSize(700, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);

        // --- Panel Superior: Información y Cambio de Estado ---
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(ConstantesUI.BORDE_TITULO("Información de la Tarea"));
        panelSuperior.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Labels y Datos
        gbc.gridx = 0; gbc.weightx = 0.2;
        gbc.gridy = 0; panelSuperior.add(createLabel("Tarea:"), gbc);
        gbc.gridy = 1; panelSuperior.add(createLabel("Prioridad:"), gbc);
        gbc.gridy = 2; panelSuperior.add(createLabel("Fecha Inicio:"), gbc);
        gbc.gridy = 3; panelSuperior.add(createLabel("Fecha Fin Estimada:"), gbc);
        gbc.gridy = 4; panelSuperior.add(createLabel("Cambiar Estado:"), gbc);
        gbc.gridy = 5; gbc.anchor = GridBagConstraints.NORTHWEST; panelSuperior.add(createLabel("Usuarios Designados:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        gbc.gridy = 0; panelSuperior.add(createDataLabel(tarea.getNombreTarea()), gbc);
        gbc.gridy = 1; panelSuperior.add(createDataLabel(tarea.getNombrePrioridad()), gbc);
        gbc.gridy = 2; panelSuperior.add(createDataLabel(tarea.getFechaInicio() != null ? sdf.format(tarea.getFechaInicio()) : "N/A"), gbc);
        gbc.gridy = 3; panelSuperior.add(createDataLabel(tarea.getFechaFinalEstimada() != null ? sdf.format(tarea.getFechaFinalEstimada()) : "N/A"), gbc);

        gbc.gridy = 4;
        comboEstado = new JComboBox<>(new Vector<>(estados));
        comboEstado.setFont(ConstantesUI.FUENTE_NORMAL);
        estados.stream().filter(e -> e.getIdEstado() == tarea.getIdEstado()).findFirst().ifPresent(comboEstado::setSelectedItem);
        panelSuperior.add(comboEstado, gbc);

        gbc.gridy = 5; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        listModelUsuarios = new DefaultListModel<>();
        JList<Usuario> listaUsuarios = new JList<>(listModelUsuarios);
        listaUsuarios.setFont(ConstantesUI.FUENTE_NORMAL);
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setBorder(ConstantesUI.BORDE_SIMPLE);
        panelSuperior.add(scrollUsuarios, gbc);

        // --- Panel de Comentarios ---
        JPanel panelComentarios = new JPanel(new BorderLayout(5, 5));
        panelComentarios.setBorder(ConstantesUI.BORDE_TITULO("Comentarios"));
        panelComentarios.setOpaque(false);

        listModelComentarios = new DefaultListModel<>();
        listaComentarios = new JList<>(listModelComentarios);

        // --- CORRECCIÓN: Usar el nuevo nombre de clase ---
        listaComentarios.setCellRenderer(new RenderizadorComentario());

        JScrollPane scrollComentarios = new JScrollPane(listaComentarios);
        scrollComentarios.setBorder(null);
        panelComentarios.add(scrollComentarios, BorderLayout.CENTER);

        JPanel panelNuevoComentario = new JPanel(new BorderLayout(5, 5));
        panelNuevoComentario.setOpaque(false);
        panelNuevoComentario.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        areaNuevoComentario = new JTextArea(3, 0);
        areaNuevoComentario.setFont(ConstantesUI.FUENTE_NORMAL);
        btnPublicarComentario = new JButton("Publicar");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnPublicarComentario);
        panelNuevoComentario.add(new JScrollPane(areaNuevoComentario), BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicarComentario, BorderLayout.EAST);
        panelComentarios.add(panelNuevoComentario, BorderLayout.SOUTH);

        // --- Panel de Botones Inferior ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelAcciones.setOpaque(false);
        btnGuardar = new JButton("Guardar Cambios");
        btnCerrar = new JButton("Cerrar");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnGuardar);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnCerrar);
        panelAcciones.add(btnCerrar);
        panelAcciones.add(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnCerrar.addActionListener(e -> setVisible(false));

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(panelComentarios, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel("<html><b>" + text + "</b></html>");
        label.setFont(ConstantesUI.FUENTE_NORMAL);
        return label;
    }

    private JLabel createDataLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(ConstantesUI.FUENTE_NORMAL);
        return label;
    }

    private void guardar() {
        this.guardado = true;
        setVisible(false);
    }

    public JComboBox<Estado> getComboEstado() { return comboEstado; }
    public DefaultListModel<Comentario> getListModelComentarios() { return listModelComentarios; }
    public DefaultListModel<Usuario> getListModelUsuarios() { return listModelUsuarios; }
    public JTextArea getAreaNuevoComentario() { return areaNuevoComentario; }
    public JButton getBtnPublicarComentario() { return btnPublicarComentario; }
    public Tarea getTarea() { return tarea; }
    public boolean isGuardado() { return guardado; }
}