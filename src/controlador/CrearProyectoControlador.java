// Archivo: src/controlador/CrearProyectoControlador.java
package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import persistencia.ProyectoDAO;
import util.SesionUsuario;
import vista.CrearProyectoVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class CrearProyectoControlador implements ActionListener {
    private final CrearProyectoVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;

    public CrearProyectoControlador(CrearProyectoVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();

        this.vista.getBtnCrearProyecto().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrearProyecto()) {
            crearProyecto();
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void crearProyecto() {
        String nombre = vista.getTxtNombreProyecto().getText().trim();
        String descripcion = vista.getTxtDescripcionProyecto().getText().trim();
        Date fechaInicio = (Date) vista.getDatePickerFechaInicio().getModel().getValue();
        Date fechaFinEstimada = (Date) vista.getDatePickerFechaFinalEstimada().getModel().getValue();
        Prioridad prioridadSeleccionada = (Prioridad) vista.getComboPrioridad().getSelectedItem();

        if (nombre.isEmpty() || prioridadSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "El nombre del proyecto y la prioridad son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fechaInicio == null) {
            JOptionPane.showMessageDialog(vista, "La fecha de inicio es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fechaFinEstimada != null && fechaFinEstimada.before(fechaInicio)) {
            JOptionPane.showMessageDialog(vista, "La fecha final estimada no puede ser anterior a la fecha de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setNombreProyecto(nombre);
        nuevoProyecto.setDescripcionProyecto(descripcion.isEmpty() ? null : descripcion);
        nuevoProyecto.setFechaInicio(fechaInicio);
        nuevoProyecto.setFechaFinalEstimada(fechaFinEstimada);
        nuevoProyecto.setIdPrioridad(prioridadSeleccionada.getIdPrioridad());

        int nuevoProyectoId = proyectoDAO.insertarYObtenerId(nuevoProyecto);

        if (nuevoProyectoId != -1) {
            Usuario usuarioLogueado = SesionUsuario.getUsuarioLogueado();
            if (usuarioLogueado != null) {
                int idJerarquiaProjectManager = 1;
                boolean designacionExitosa = proyectoDAO.insertarDesignacionProyecto(usuarioLogueado.getIdUsuario(), nuevoProyectoId, idJerarquiaProjectManager);
                if (!designacionExitosa) {
                    JOptionPane.showMessageDialog(vista, "Error al asignar el usuario logueado como Project Manager.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "No hay usuario logueado para asignar como Project Manager.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(vista, "Proyecto creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al crear el proyecto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        vista.getTxtNombreProyecto().setText("");
        vista.getTxtDescripcionProyecto().setText("");
        vista.getDatePickerFechaInicio().getModel().setValue(null);
        vista.getDatePickerFechaFinalEstimada().getModel().setValue(null);
        vista.getComboPrioridad().setSelectedIndex(0);
    }
}