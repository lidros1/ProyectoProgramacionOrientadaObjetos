// Archivo: src/controlador/ExploradorTareasControlador.java
package controlador;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Tarea;
import modelo.Usuario;
import persistencia.DesignacionDAO;
import persistencia.TareaDAO;
import vista.DialogoEditarTarea;
import vista.ExploradorTareasVista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class ExploradorTareasControlador {
    private final ExploradorTareasVista vista;
    private final JFrame vistaAnterior;
    private final TareaDAO tareaDAO;
    private final DesignacionDAO designacionDAO;
    private List<Tarea> todasLasTareas;

    // Listas de datos para pasar al diálogo de edición
    private final List<Prioridad> prioridades;
    private final List<Estado> estados;
    private final List<Usuario> usuarios;

    public ExploradorTareasControlador(ExploradorTareasVista vista, JFrame vistaAnterior, List<Prioridad> prioridades, List<Estado> estados, List<Usuario> usuarios) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.tareaDAO = new TareaDAO();
        this.designacionDAO = new DesignacionDAO();
        this.todasLasTareas = tareaDAO.listarTodasLasTareas();

        this.prioridades = prioridades;
        this.estados = estados;
        this.usuarios = usuarios;

        // Listeners
        this.vista.getBtnVolver().addActionListener(e -> cerrar());
        this.vista.getComboFiltroProyecto().addActionListener(e -> filtrarYMostrarTareas());
        this.vista.getComboFiltroPrioridad().addActionListener(e -> filtrarYMostrarTareas());

        this.vista.getTxtBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
        });

        this.vista.getListaTareas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDialogoEdicion();
                }
            }
        });

        filtrarYMostrarTareas();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void cerrar() {
        vista.dispose();
        vistaAnterior.setVisible(true);
    }

    private void filtrarYMostrarTareas() {
        String textoBusqueda = vista.getTxtBusqueda().getText().toLowerCase();
        Proyecto proySeleccionado = (Proyecto) vista.getComboFiltroProyecto().getSelectedItem();
        Prioridad prioSeleccionada = (Prioridad) vista.getComboFiltroPrioridad().getSelectedItem();

        List<Tarea> tareasFiltradas = todasLasTareas.stream()
                .filter(t -> proySeleccionado == null || proySeleccionado.getIdProyecto() == 0 || t.getIdProyecto() == proySeleccionado.getIdProyecto())
                .filter(t -> prioSeleccionada == null || prioSeleccionada.getIdPrioridad() == 0 || t.getIdPrioridad() == prioSeleccionada.getIdPrioridad())
                .filter(t -> t.getNombreTarea().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());

        DefaultListModel<Tarea> model = vista.getListModelTareas();
        model.clear();
        tareasFiltradas.forEach(model::addElement);
    }

    private void abrirDialogoEdicion() {
        Tarea tareaSeleccionada = vista.getListaTareas().getSelectedValue();
        if (tareaSeleccionada == null) return;

        // Cargar usuarios asignados a la tarea
        tareaSeleccionada.setUsuariosDesignados(designacionDAO.listarUsuariosDesignadosATarea(tareaSeleccionada.getIdTarea()));

        DialogoEditarTarea dialogo = new DialogoEditarTarea(vista, tareaSeleccionada, prioridades, estados, usuarios);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Tarea tareaActualizada = dialogo.getTareaActualizada();
            boolean exitoTarea = tareaDAO.actualizar(tareaActualizada);

            // Actualizar designaciones
            designacionDAO.eliminarDesignacionesPorTarea(tareaActualizada.getIdTarea());
            if (!tareaActualizada.getUsuariosDesignados().isEmpty()) {
                designacionDAO.insertarDesignacionesTarea(tareaActualizada.getIdTarea(), tareaActualizada.getUsuariosDesignados());
            }

            if (exitoTarea) {
                JOptionPane.showMessageDialog(vista, "Tarea actualizada correctamente.");
                // Refrescar la lista de tareas
                this.todasLasTareas = tareaDAO.listarTodasLasTareas();
                filtrarYMostrarTareas();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar la tarea.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}