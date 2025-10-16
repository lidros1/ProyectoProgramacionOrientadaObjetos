package controlador;

import modelo.*;
import persistencia.*;
import util.SesionUsuario;
import vista.CrearTareaVista;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CrearTareaControlador implements ActionListener {
    private final CrearTareaVista vista;
    private final JFrame vistaAnterior;
    private final TareaDAO tareaDAO;
    private final ComentarioDAO comentarioDAO;
    private final DesignacionDAO designacionDAO;
    private final List<Proyecto> todosLosProyectos;

    public CrearTareaControlador(CrearTareaVista vista, JFrame vistaAnterior, List<Proyecto> proyectos) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.tareaDAO = new TareaDAO();
        this.comentarioDAO = new ComentarioDAO();
        this.designacionDAO = new DesignacionDAO();
        this.todosLosProyectos = proyectos;

        // Cargar proyectos en la JList
        DefaultListModel<Proyecto> modelProyectos = vista.getListModelProyectos();
        proyectos.forEach(modelProyectos::addElement);

        // Action Listeners
        this.vista.getBtnCrear().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);

        // Listeners para búsqueda y selección de proyectos
        this.vista.getTxtBuscarProyecto().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { buscarProyectos(); }
            @Override public void removeUpdate(DocumentEvent e) { buscarProyectos(); }
            @Override public void changedUpdate(DocumentEvent e) { buscarProyectos(); }
        });
        this.vista.getListaProyectos().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                setFormularioHabilitado(vista.getListaProyectos().getSelectedValue() != null);
            }
        });

        // --- MODIFICACIÓN: Se añaden listeners de doble clic ---
        this.vista.getListaUsuariosDisponibles().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    moverUsuario(vista.getListaUsuariosDisponibles(), vista.getListaUsuariosAsignados());
                }
            }
        });
        this.vista.getListaUsuariosAsignados().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    moverUsuario(vista.getListaUsuariosAsignados(), vista.getListaUsuariosDisponibles());
                }
            }
        });

        setFormularioHabilitado(false);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrear()) {
            crearTarea();
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
        // Se elimina la lógica para los botones de flecha
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

    private void setFormularioHabilitado(boolean habilitado) {
        habilitarComponentes(vista.getPanelFormulario(), habilitado);
        vista.getBtnCrear().setEnabled(habilitado);
    }

    private void habilitarComponentes(java.awt.Component component, boolean enable) {
        component.setEnabled(enable);
        if (component instanceof Container) {
            for (java.awt.Component child : ((Container) component).getComponents()) {
                habilitarComponentes(child, enable);
            }
        }
    }

    private void crearTarea() {
        Proyecto proyectoSeleccionado = vista.getListaProyectos().getSelectedValue();
        String nombreTarea = vista.getTxtNombreTarea().getText().trim();
        Prioridad prioridad = (Prioridad) vista.getComboPrioridad().getSelectedItem();

        if (proyectoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un proyecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nombreTarea.isEmpty() || prioridad == null) {
            JOptionPane.showMessageDialog(vista, "El nombre de la tarea y la prioridad son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setIdProyecto(proyectoSeleccionado.getIdProyecto());
        nuevaTarea.setNombreTarea(nombreTarea);
        nuevaTarea.setIdPrioridad(prioridad.getIdPrioridad());
        nuevaTarea.setFechaInicio((Date) vista.getDatePickerInicio().getModel().getValue());
        nuevaTarea.setFechaFinalEstimada((Date) vista.getDatePickerFin().getModel().getValue());

        int nuevaTareaId = tareaDAO.insertarYObtenerId(nuevaTarea);

        if (nuevaTareaId != -1) {
            String textoComentario = vista.getAreaComentario().getText().trim();
            if (!textoComentario.isEmpty()) {
                Comentario comentario = new Comentario();
                comentario.setIdTarea(nuevaTareaId);
                comentario.setIdUsuario(SesionUsuario.getUsuarioLogueado().getIdUsuario());
                comentario.setContenido(textoComentario);
                comentarioDAO.insertar(comentario);
            }

            DefaultListModel<Usuario> modelAsignados = vista.getModelAsignados();
            if (modelAsignados.getSize() > 0) {
                List<Usuario> usuariosAsignados = new ArrayList<>();
                for (int i = 0; i < modelAsignados.getSize(); i++) {
                    usuariosAsignados.add(modelAsignados.getElementAt(i));
                }
                designacionDAO.insertarDesignacionesTarea(nuevaTareaId, usuariosAsignados);
            }

            JOptionPane.showMessageDialog(vista, "Tarea creada exitosamente.");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al crear la tarea.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moverUsuario(JList<Usuario> origen, JList<Usuario> destino) {
        Usuario seleccionado = origen.getSelectedValue();
        if (seleccionado == null) return;

        ((DefaultListModel<Usuario>) origen.getModel()).removeElement(seleccionado);
        ((DefaultListModel<Usuario>) destino.getModel()).addElement(seleccionado);
    }

    private void limpiarFormulario() {
        vista.getTxtNombreTarea().setText("");
        vista.getAreaComentario().setText("");
        vista.getDatePickerInicio().getModel().setValue(null);
        vista.getDatePickerFin().getModel().setValue(null);
        vista.getComboPrioridad().setSelectedIndex(0);

        DefaultListModel<Usuario> modelDisponibles = vista.getModelDisponibles();
        DefaultListModel<Usuario> modelAsignados = vista.getModelAsignados();
        for (int i = 0; i < modelAsignados.getSize(); i++) {
            modelDisponibles.addElement(modelAsignados.getElementAt(i));
        }
        modelAsignados.clear();
    }
}