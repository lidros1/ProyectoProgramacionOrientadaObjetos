package controlador;

import modelo.Usuario;
import persistencia.UsuarioDAO;
import vista.ListarUsuarioVista;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class ListarUsuarioControlador implements ActionListener {
    private final ListarUsuarioVista vista;
    private final JFrame vistaAnterior;
    private final UsuarioDAO usuarioDAO;

    public ListarUsuarioControlador(ListarUsuarioVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.usuarioDAO = new UsuarioDAO();

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getTxtBuscar().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { buscarUsuarios(); }
            @Override
            public void removeUpdate(DocumentEvent e) { buscarUsuarios(); }
            @Override
            public void changedUpdate(DocumentEvent e) { buscarUsuarios(); }
        });

        this.vista.getListaResultados().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallesUsuarioSeleccionado();
            }
        });

        // Carga inicial
        buscarUsuarios();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void buscarUsuarios() {
        String terminoBusqueda = vista.getTxtBuscar().getText();
        List<Usuario> usuarios = usuarioDAO.buscarPorNombre(terminoBusqueda);

        DefaultListModel<Usuario> model = vista.getListModel();
        model.clear();
        for (Usuario u : usuarios) {
            model.addElement(u);
        }
    }

    private void mostrarDetallesUsuarioSeleccionado() {
        Usuario usuarioSeleccionado = vista.getListaResultados().getSelectedValue();
        if (usuarioSeleccionado == null) {
            limpiarDetalles();
            return;
        }

        Usuario usuarioCompleto = usuarioDAO.obtenerUsuarioCompletoPorId(usuarioSeleccionado.getIdUsuario());

        if (usuarioCompleto != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            vista.getLblNombre().setText(usuarioCompleto.getNombreUsuario());
            vista.getLblMail().setText(usuarioCompleto.getMail());
            vista.getLblFechaCreacion().setText(sdf.format(usuarioCompleto.getFechaCreacion()));
            vista.getLblUltimoAcceso().setText(
                    usuarioCompleto.getFechaUltimoAcceso() != null ? sdf.format(usuarioCompleto.getFechaUltimoAcceso()) : "Nunca"
            );
        }
    }

    private void limpiarDetalles() {
        vista.getLblNombre().setText("-");
        vista.getLblMail().setText("-");
        vista.getLblFechaCreacion().setText("-");
        vista.getLblUltimoAcceso().setText("-");
    }
}