package controlador;

import modelo.Proyecto;
import modelo.Tarea;
import modelo.Usuario;
import persistencia.*;
import vista.AsignarTareaVista;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AsignarTareaControlador implements ActionListener {
    private final AsignarTareaVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final TareaDAO tareaDAO;
    private final UsuarioDAO usuarioDAO;
    private final DesignacionDAO designacionDAO;

    private List<Proyecto> todosLosProyectos;
    private List<Usuario> todosLosUsuarios;
    private List<Tarea> tareasDelProyectoSeleccionado;
    private Tarea tareaSeleccionada;

    public AsignarTareaControlador(AsignarTareaVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.tareaDAO = new TareaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.designacionDAO = new DesignacionDAO();

        this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
        this.todosLosUsuarios = usuarioDAO.listarTodos();

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getBtnGuardar().addActionListener(this);
        this.vista.getBtnVolverAProyectos().addActionListener(this);
        this.vista.getTxtBuscarProyecto().getDocument().addDocumentListener(createSearchListener(this::buscarProyectos));
        this.vista.getListaProyectos().addMouseListener(createDobleClicListener(this::seleccionarProyecto));
        this.vista.getListaTareas().addMouseListener(createDobleClicListener(this::seleccionarTarea));
        this.vista.getListaUsuariosDisponibles().addMouseListener(createDobleClicListener(() -> moverUsuario(vista.getListaUsuariosDisponibles(), vista.getListaUsuariosAsignados())));
        this.vista.getListaUsuariosAsignados().addMouseListener(createDobleClicListener(() -> moverUsuario(vista.getListaUsuariosAsignados(), vista.getListaUsuariosDisponibles())));

        buscarProyectos();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        } else if (e.getSource() == vista.getBtnGuardar()) {
            guardarDesignaciones();
        } else if (e.getSource() == vista.getBtnVolverAProyectos()) {
            vista.getCardLayoutIzquierdo().show(vista.getPanelIzquierdo(), "PROYECTOS");
            vista.getPanelAsignacion().setVisible(false);
        }
    }

    private void buscarProyectos() {
        String termino = vista.getTxtBuscarProyecto().getText().toLowerCase();
        List<Proyecto> proyectosFiltrados = todosLosProyectos.stream()
                .filter(p -> p.getNombreProyecto().toLowerCase().contains(termino))
                .collect(Collectors.toList());

        vista.getModelProyectos().clear();
        proyectosFiltrados.forEach(vista.getModelProyectos()::addElement);
    }

    private void seleccionarProyecto() {
        Proyecto proyecto = vista.getListaProyectos().getSelectedValue();
        if (proyecto == null) return;

        tareasDelProyectoSeleccionado = tareaDAO.listarTareasPorProyecto(proyecto.getIdProyecto());
        vista.getModelTareas().clear();
        tareasDelProyectoSeleccionado.forEach(vista.getModelTareas()::addElement);

        vista.getCardLayoutIzquierdo().show(vista.getPanelIzquierdo(), "TAREAS");
        vista.getPanelAsignacion().setVisible(false);
    }

    private void seleccionarTarea() {
        tareaSeleccionada = vista.getListaTareas().getSelectedValue();
        if (tareaSeleccionada == null) {
            vista.getPanelAsignacion().setVisible(false);
            return;
        }

        vista.getLblTareaSeleccionada().setText("Tarea: " + tareaSeleccionada.getNombreTarea());

        List<Usuario> usuariosAsignados = designacionDAO.listarUsuariosDesignadosATarea(tareaSeleccionada.getIdTarea());
        List<Integer> idsAsignados = usuariosAsignados.stream().map(Usuario::getIdUsuario).collect(Collectors.toList());

        vista.getModelDisponibles().clear();
        vista.getModelAsignados().clear();

        todosLosUsuarios.forEach(u -> {
            if (idsAsignados.contains(u.getIdUsuario())) {
                vista.getModelAsignados().addElement(u);
            } else {
                vista.getModelDisponibles().addElement(u);
            }
        });

        vista.getPanelAsignacion().setVisible(true);
        // --- CORRECCIÓN AQUÍ ---
        vista.getSplitPane().resetToPreferredSizes();
    }

    private void guardarDesignaciones() {
        if (tareaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "No hay una tarea seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultListModel<Usuario> modelAsignados = vista.getModelAsignados();
        List<Usuario> nuevosAsignados = new ArrayList<>();
        for (int i = 0; i < modelAsignados.getSize(); i++) {
            nuevosAsignados.add(modelAsignados.getElementAt(i));
        }

        designacionDAO.eliminarDesignacionesPorTarea(tareaSeleccionada.getIdTarea());
        boolean exito = true;
        if (!nuevosAsignados.isEmpty()) {
            exito = designacionDAO.insertarDesignacionesTarea(tareaSeleccionada.getIdTarea(), nuevosAsignados);
        }

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Asignaciones guardadas correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar las asignaciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moverUsuario(JList<Usuario> origen, JList<Usuario> destino) {
        Usuario seleccionado = origen.getSelectedValue();
        if (seleccionado == null) return;

        ((DefaultListModel<Usuario>) origen.getModel()).removeElement(seleccionado);
        ((DefaultListModel<Usuario>) destino.getModel()).addElement(seleccionado);
    }

    private DocumentListener createSearchListener(Runnable action) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { action.run(); }
            @Override public void removeUpdate(DocumentEvent e) { action.run(); }
            @Override public void changedUpdate(DocumentEvent e) { action.run(); }
        };
    }

    private MouseAdapter createDobleClicListener(Runnable action) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    action.run();
                }
            }
        };
    }
}