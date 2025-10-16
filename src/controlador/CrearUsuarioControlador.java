package controlador;

import modelo.Permiso;
import modelo.Usuario;
import persistencia.PermisoDAO;
import persistencia.UsuarioDAO;
import vista.CrearUsuarioVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrearUsuarioControlador implements ActionListener {
    private final CrearUsuarioVista vista;
    private final JFrame vistaAnterior;
    private final UsuarioDAO usuarioDAO;
    private final PermisoDAO permisoDAO;

    public CrearUsuarioControlador(CrearUsuarioVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.usuarioDAO = new UsuarioDAO();
        this.permisoDAO = new PermisoDAO();

        this.vista.getBtnCrear().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrear()) {
            crearUsuario();
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void crearUsuario() {
        String nombre = vista.getTxtNombreUsuario().getText().trim();
        String contrasena = new String(vista.getTxtContrasena().getPassword()).trim();
        String mail = vista.getTxtMail().getText().trim();

        if (nombre.isEmpty() || contrasena.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, contrasena, mail);
        int nuevoUsuarioId = usuarioDAO.insertarYObtenerId(nuevoUsuario);

        if (nuevoUsuarioId != -1) {
            // Usuario creado, ahora insertamos los permisos
            List<Permiso> permisosSeleccionados = obtenerPermisosSeleccionados();
            if (!permisosSeleccionados.isEmpty()) {
                permisoDAO.insertarPermisos(nuevoUsuarioId, permisosSeleccionados);
            }
            JOptionPane.showMessageDialog(vista, "Usuario creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al crear el usuario. El nombre de usuario o email ya podría existir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Permiso> obtenerPermisosSeleccionados() {
        List<Permiso> permisos = new ArrayList<>();
        Map<String, JCheckBox> checkboxes = vista.getCheckboxesPermisos();

        for (Map.Entry<String, JCheckBox> entry : checkboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                String[] ids = entry.getKey().split("-");
                int idArea = Integer.parseInt(ids[0]);
                int idFuncion = Integer.parseInt(ids[1]);

                Permiso p = new Permiso();
                p.setIdAreaSistema(idArea);
                p.setIdFuncion(idFuncion);
                permisos.add(p);
            }
        }
        return permisos;
    }

    private void limpiarFormulario() {
        vista.getTxtNombreUsuario().setText("");
        vista.getTxtContrasena().setText("");
        vista.getTxtMail().setText("");
        for (JCheckBox checkBox : vista.getCheckboxesPermisos().values()) {
            checkBox.setSelected(false);
        }
    }
}