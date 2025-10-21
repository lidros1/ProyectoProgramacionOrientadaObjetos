// Archivo: src/controlador/EditarUsuarioControlador.java
package controlador;

import modelo.Permiso;
import modelo.Usuario;
import persistencia.PermisoDAO;
import persistencia.UsuarioDAO;
import vista.EditarUsuarioVista;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditarUsuarioControlador implements ActionListener {
    private final EditarUsuarioVista vista;
    private final JFrame vistaAnterior;
    private final UsuarioDAO usuarioDAO;
    private final PermisoDAO permisoDAO;
    private Usuario usuarioSeleccionado;

    public EditarUsuarioControlador(EditarUsuarioVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.usuarioDAO = new UsuarioDAO();
        this.permisoDAO = new PermisoDAO();

        this.vista.getBtnGuardar().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getTxtBuscar().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { buscarUsuarios(); }
            @Override public void removeUpdate(DocumentEvent e) { buscarUsuarios(); }
            @Override public void changedUpdate(DocumentEvent e) { buscarUsuarios(); }
        });
        this.vista.getListaResultados().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosUsuarioSeleccionado();
            }
        });

        buscarUsuarios();
        limpiarPanelEdicion();
        this.vista.getPanelEdicion().setVisible(true);
        this.vista.getSplitPane().setDividerLocation(350);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnGuardar()) {
            guardarCambios();
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void buscarUsuarios() {
        String termino = vista.getTxtBuscar().getText();
        List<Usuario> usuarios = usuarioDAO.buscarPorNombre(termino);
        vista.getListModel().clear();
        usuarios.forEach(vista.getListModel()::addElement);

        usuarioSeleccionado = null;
        limpiarPanelEdicion();
        this.vista.getSplitPane().setDividerLocation(350);
    }

    private void cargarDatosUsuarioSeleccionado() {
        usuarioSeleccionado = vista.getListaResultados().getSelectedValue();
        if (usuarioSeleccionado == null) {
            limpiarPanelEdicion();
            return;
        }

        vista.getTxtNombreUsuario().setText(usuarioSeleccionado.getNombreUsuario());
        Usuario usuarioCompleto = usuarioDAO.obtenerUsuarioCompletoPorId(usuarioSeleccionado.getIdUsuario());

        if (usuarioCompleto != null) {
            vista.getComboActivo().setSelectedItem(usuarioCompleto.getEstado());
        } else {
            vista.getComboActivo().setSelectedIndex(0);
        }
        vista.getTxtContrasena().setText("");

        List<Permiso> permisosActuales = permisoDAO.listarPermisosPorUsuario(usuarioSeleccionado.getIdUsuario());
        Map<String, JCheckBox> checkboxes = vista.getCheckboxesPermisos();

        checkboxes.values().forEach(cb -> cb.setSelected(false));

        for (Permiso p : permisosActuales) {
            String key = p.getIdAreaSistema() + "-" + p.getIdFuncion();
            if (checkboxes.containsKey(key)) {
                checkboxes.get(key).setSelected(true);
            }
        }

        vista.getPanelEdicion().setVisible(true);
        vista.getSplitPane().setDividerLocation(350);
        vista.revalidate();
        vista.repaint();
    }

    private void guardarCambios() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "No hay un usuario seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = vista.getTxtNombreUsuario().getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre de usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioSeleccionado.setNombreUsuario(nombre);
        String nuevaContrasena = new String(vista.getTxtContrasena().getPassword()).trim();
        usuarioSeleccionado.setContrasena(nuevaContrasena);
        usuarioSeleccionado.setEstado((String) vista.getComboActivo().getSelectedItem());

        boolean datosActualizados = usuarioDAO.actualizar(usuarioSeleccionado);

        permisoDAO.eliminarPermisosPorUsuario(usuarioSeleccionado.getIdUsuario());
        List<Permiso> permisosNuevos = new ArrayList<>();
        vista.getCheckboxesPermisos().forEach((key, checkBox) -> {
            if (checkBox.isSelected()) {
                String[] ids = key.split("-");
                Permiso p = new Permiso();
                p.setIdAreaSistema(Integer.parseInt(ids[0]));
                p.setIdFuncion(Integer.parseInt(ids[1]));
                permisosNuevos.add(p);
            }
        });

        boolean permisosActualizados = !permisosNuevos.isEmpty() ? permisoDAO.insertarPermisos(usuarioSeleccionado.getIdUsuario(), permisosNuevos) : true;

        if (datosActualizados && permisosActualizados) {
            JOptionPane.showMessageDialog(vista, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            buscarUsuarios();
            limpiarPanelEdicion();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarPanelEdicion() {
        vista.getTxtNombreUsuario().setText("");
        vista.getTxtContrasena().setText("");
        vista.getComboActivo().setSelectedIndex(0);
        vista.getCheckboxesPermisos().values().forEach(cb -> cb.setSelected(false));
    }
}