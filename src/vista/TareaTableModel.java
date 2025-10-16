package vista;

import modelo.Tarea;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TareaTableModel extends AbstractTableModel {
    private List<Tarea> listaTareas;
    private final String[] columnas = {"ID Tarea", "Nombre de la Tarea", "ID Proyecto"};

    public TareaTableModel(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @Override
    public int getRowCount() { return listaTareas.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tarea tarea = listaTareas.get(rowIndex);
        switch (columnIndex) {
            case 0: return tarea.getIdTarea();
            case 1: return tarea.getNombreTarea();
            case 2: return tarea.getIdProyecto();
            default: return null;
        }
    }
}