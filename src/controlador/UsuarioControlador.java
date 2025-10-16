package controlador;

import modelo.Usuario;
import persistencia.UsuarioDAO;
import vista.GestionUsuariosVista;
import vista.UsuarioTableModel; // Se necesita para el tableModel

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// NOTA IMPORTANTE: Esta clase de UsuarioControlador está pensada para una vista de "Gestion de Usuarios"
// que contiene un CRUD. Si la estás usando para el flujo post-login, el flujo principal debería
// ser al DashboardKanbanVista como te indiqué en pasos anteriores.
public class UsuarioControlador implements ActionListener {

    private final GestionUsuariosVista vista;
    private final UsuarioDAO usuarioDAO;

    public UsuarioControlador(GestionUsuariosVista vista, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        // Registrar este controlador como listener de los botones de la vista
        // Asegúrate de que tu GestionUsuariosVista tenga estos botones
        this.vista.getBotonNuevo().addActionListener(this);
        this.vista.getBotonEditar().addActionListener(this);
        this.vista.getBotonEliminar().addActionListener(this);
    }

    // Inicia la aplicación: carga los datos y hace visible la ventana.
    public void iniciar() {
        actualizarTabla();
        vista.setVisible(true);
    }

    // Recarga los datos de la base de datos y los muestra en la tabla.
    private void actualizarTabla() {
        // CORRECCIÓN: Asegúrate de que el método 'listarTodos()' exista en persistencia.UsuarioDAO
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        UsuarioTableModel tableModel = new UsuarioTableModel(usuarios);
        vista.setTableModel(tableModel);
    }

    // Maneja los eventos de los botones.
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

        String email = JOptionPane.showInputDialog(vista, "Email:", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;

        String contrasena = JOptionPane.showInputDialog(vista, "Contraseña:", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
        if (contrasena == null || contrasena.trim().isEmpty()) return;

        Usuario nuevoUsuario = new Usuario(nombre, contrasena, email);
        // Aquí deberías establecer una jerarquía por defecto si es requerida por tu tabla 'usuarios'
        // nuevoUsuario.setIdGerarquiaUsuario(3); // Ejemplo: ID 3 para Developer

        if (usuarioDAO.insertar(nuevoUsuario)) {
            JOptionPane.showMessageDialog(vista, "Usuario creado exitosamente.");
            actualizarTabla(); // Refrescar la tabla
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

        String email = JOptionPane.showInputDialog(vista, "Email:", usuarioSeleccionado.getMail());
        if (email == null) return;

        String contrasena = JOptionPane.showInputDialog(vista, "Nueva contraseña (dejar en blanco para no cambiar):");

        usuarioSeleccionado.setNombreUsuario(nombre);
        usuarioSeleccionado.setMail(email);
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuarioSeleccionado.setContrasena(contrasena);
        }
        // Si la jerarquía se actualiza aquí, también se debería pedir
        // usuarioSeleccionado.setIdGerarquiaUsuario(nuevaJerarquia);

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