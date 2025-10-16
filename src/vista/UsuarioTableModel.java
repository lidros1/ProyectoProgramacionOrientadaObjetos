// Archivo: src/vista/UsuarioTableModel.java
package vista;

import modelo.Usuario;
import javax.swing.table.AbstractTableModel;
import java.util.List;

// Este es el Modelo para nuestra JTable. Le dice a la tabla cómo obtener los datos
// de la lista de usuarios.
public class UsuarioTableModel extends AbstractTableModel {

    private final List<Usuario> listaUsuarios;
    private final String[] columnas = {"ID", "Nombre de Usuario", "Email"};

    public UsuarioTableModel(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getRowCount() {
        return listaUsuarios.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario usuario = listaUsuarios.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return usuario.getIdUsuario();
            case 1:
                return usuario.getNombreUsuario();
            case 2:
                return usuario.getMail();
            default:
                return null;
        }
    }

    // Método para obtener el objeto Usuario completo de una fila específica
    public Usuario getUsuarioAt(int rowIndex) {
        return listaUsuarios.get(rowIndex);
    }
}