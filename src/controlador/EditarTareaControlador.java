package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import persistencia.PrioridadDAO;
import persistencia.ProyectoDAO;
import vista.EditarTareaVista;
import vista.ProyectoDetalleVista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class EditarTareaControlador implements ActionListener {
    private final EditarTareaVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final PrioridadDAO prioridadDAO;
    private List<Proyecto> todosLosProyectos;

    public EditarTareaControlador(EditarTareaVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.prioridadDAO = new PrioridadDAO();
        this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getTxtBuscarProyecto().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { buscarProyectos(); }
            @Override public void removeUpdate(DocumentEvent e) { buscarProyectos(); }
            @Override public void changedUpdate(DocumentEvent e) { buscarProyectos(); }
        });

        // Listener de doble clic para seleccionar
        this.vista.getListaProyectos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    seleccionarProyecto();
                }
            }
        });

        buscarProyectos(); // Carga inicial
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

    private void buscarProyectos() {
        String termino = vista.getTxtBuscarProyecto().getText().toLowerCase();
        List<Proyecto> proyectosFiltrados = todosLosProyectos.stream()
                .filter(p -> p.getNombreProyecto().toLowerCase().contains(termino))
                .collect(Collectors.toList());

        DefaultListModel<Proyecto> model = vista.getListModelProyectos();
        model.clear();
        proyectosFiltrados.forEach(model::addElement);
    }

    private void seleccionarProyecto() {
        Proyecto proyectoSeleccionado = vista.getListaProyectos().getSelectedValue();
        if (proyectoSeleccionado == null) {
            return;
        }

        vista.dispose(); // Cierra la ventana de selección

        List<Prioridad> prioridades = prioridadDAO.listarTodos();
        ProyectoDetalleVista detalleVista = new ProyectoDetalleVista(proyectoSeleccionado, prioridades);

        // Creamos el controlador de detalle en MODO EDICIÓN COMPLETA (true)
        ProyectoDetalleControlador detalleControlador = new ProyectoDetalleControlador(detalleVista, vistaAnterior, true);
        detalleControlador.iniciar();
    }
}