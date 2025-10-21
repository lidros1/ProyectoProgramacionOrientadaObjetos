// Archivo: src/vista/UsuarioTableModel.java
package vista;

import modelo.Usuario;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UsuarioTableModel extends AbstractTableModel {

    private final List<Usuario> listaUsuarios;
    private final String[] columnas = {"ID", "Nombre de Usuario", "Estado"};

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
                return usuario.getEstado();
            default:
                return null;
        }
    }

    public Usuario getUsuarioAt(int rowIndex) {
        return listaUsuarios.get(rowIndex);
    }
}