// Archivo: src/controlador/UsuarioControlador.java
package controlador;

import modelo.Usuario;
import persistencia.UsuarioDAO;
import vista.GestionUsuariosVista;
import vista.UsuarioTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioControlador implements ActionListener {

    private final GestionUsuariosVista vista;
    private final UsuarioDAO usuarioDAO;

    public UsuarioControlador(GestionUsuariosVista vista, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        this.vista.getBotonNuevo().addActionListener(this);
        this.vista.getBotonEditar().addActionListener(this);
        this.vista.getBotonEliminar().addActionListener(this);
    }

    public void iniciar() {
        actualizarTabla();
        vista.setVisible(true);
    }

    private void actualizarTabla() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        UsuarioTableModel tableModel = new UsuarioTableModel(usuarios);
        vista.setTableModel(tableModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBotonNuevo()) {
            crearNuevoUsuario();
        } else if (e.getSource() == vista.getBotonEditar()) {
            editarUsuarioSeleccionado();
        } else if (e.getSource() == vista.getBotonEliminar()) {
            eliminarUsuarioSeleccionado();
        }
    }

    private void crearNuevoUsuario() {
        String nombre = JOptionPane.showInputDialog(vista, "Nombre de usuario:", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) return;

        String contrasena = JOptionPane.showInputDialog(vista, "Contraseña:", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
        if (contrasena == null || contrasena.trim().isEmpty()) return;

        Usuario nuevoUsuario = new Usuario(nombre, contrasena);

        if (usuarioDAO.insertarYObtenerId(nuevoUsuario) != -1) {
            JOptionPane.showMessageDialog(vista, "Usuario creado exitosamente.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al crear el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarUsuarioSeleccionado() {
        int filaSeleccionada = vista.getTablaUsuarios().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un usuario para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioTableModel model = (UsuarioTableModel) vista.getTablaUsuarios().getModel();
        Usuario usuarioSeleccionado = model.getUsuarioAt(filaSeleccionada);

        String nombre = JOptionPane.showInputDialog(vista, "Nombre de usuario:", usuarioSeleccionado.getNombreUsuario());
        if (nombre == null) return;

        String contrasena = JOptionPane.showInputDialog(vista, "Nueva contraseña (dejar en blanco para no cambiar):");

        usuarioSeleccionado.setNombreUsuario(nombre);
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuarioSeleccionado.setContrasena(contrasena);
        }

        if (usuarioDAO.actualizar(usuarioSeleccionado)) {
            JOptionPane.showMessageDialog(vista, "Usuario actualizado exitosamente.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = vista.getTablaUsuarios().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea eliminar a este usuario?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            UsuarioTableModel model = (UsuarioTableModel) vista.getTablaUsuarios().getModel();
            Usuario usuarioSeleccionado = model.getUsuarioAt(filaSeleccionada);

            if (usuarioDAO.eliminar(usuarioSeleccionado.getIdUsuario())) {
                JOptionPane.showMessageDialog(vista, "Usuario eliminado exitosamente.");
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}