package controlador;

import modelo.Proyecto;
import modelo.Usuario;
import persistencia.DesignacionDAO;
import persistencia.ProyectoDAO;
import persistencia.UsuarioDAO;
import vista.DesignarUsuariosProyectoVista;

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

public class DesignarUsuariosProyectoControlador implements ActionListener {
    private final DesignarUsuariosProyectoVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final UsuarioDAO usuarioDAO;
    private final DesignacionDAO designacionDAO;

    private List<Proyecto> todosLosProyectos;
    private List<Usuario> todosLosUsuarios;
    private Proyecto proyectoSeleccionado;

    public DesignarUsuariosProyectoControlador(DesignarUsuariosProyectoVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.designacionDAO = new DesignacionDAO();

        this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
        this.todosLosUsuarios = usuarioDAO.listarTodos();

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getBtnGuardar().addActionListener(this);
        this.vista.getTxtBuscarProyecto().getDocument().addDocumentListener(createSearchListener(this::buscarProyectos));
        this.vista.getListaProyectos().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPanelAsignacion();
            }
        });

        // Listener para doble clic
        this.vista.getListaUsuariosDisponibles().addMouseListener(createDoubleClickMoverListener(vista.getListaUsuariosDisponibles(), vista.getListaUsuariosAsignados()));
        this.vista.getListaUsuariosAsignados().addMouseListener(createDoubleClickMoverListener(vista.getListaUsuariosAsignados(), vista.getListaUsuariosDisponibles()));

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

        // --- CORRECCIÓN: Ocultar el panel al buscar ---
        vista.getPanelAsignacion().setVisible(false);
    }

    private void cargarPanelAsignacion() {
        proyectoSeleccionado = vista.getListaProyectos().getSelectedValue();
        if (proyectoSeleccionado == null) {
            vista.getPanelAsignacion().setVisible(false);
            return;
        }

        vista.getLblProyectoSeleccionado().setText("Asignando equipo a: " + proyectoSeleccionado.getNombreProyecto());

        List<Usuario> usuariosAsignados = designacionDAO.listarUsuariosDesignadosAProyecto(proyectoSeleccionado.getIdProyecto());
        List<Integer> idsAsignados = usuariosAsignados.stream().map(Usuario::getIdUsuario).collect(Collectors.toList());

        DefaultListModel<Usuario> modelDisponibles = vista.getModelDisponibles();
        DefaultListModel<Usuario> modelAsignados = vista.getModelAsignados();
        modelDisponibles.clear();
        modelAsignados.clear();

        for (Usuario u : todosLosUsuarios) {
            if (idsAsignados.contains(u.getIdUsuario())) {
                modelAsignados.addElement(u);
            } else {
                modelDisponibles.addElement(u);
            }
        }

        // --- CORRECCIÓN: Hacer visible el panel y forzar la actualización del layout ---
        vista.getPanelAsignacion().setVisible(true);
        vista.getSplitPane().resetToPreferredSizes();
    }

    private void guardarDesignaciones() {
        if (proyectoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un proyecto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultListModel<Usuario> modelAsignados = vista.getModelAsignados();
        List<Usuario> nuevosAsignados = new ArrayList<>();
        for (int i = 0; i < modelAsignados.getSize(); i++) {
            nuevosAsignados.add(modelAsignados.getElementAt(i));
        }

        designacionDAO.eliminarDesignacionesPorProyecto(proyectoSeleccionado.getIdProyecto());
        boolean exito = true;
        if (!nuevosAsignados.isEmpty()) {
            exito = designacionDAO.insertarDesignaciones(proyectoSeleccionado.getIdProyecto(), nuevosAsignados);
        }

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Designaciones guardadas correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar las designaciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos de Ayuda ---
    private void moverUsuarios(JList<Usuario> origen, JList<Usuario> destino) {
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

    private MouseAdapter createDoubleClickMoverListener(JList<Usuario> origen, JList<Usuario> destino) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    moverUsuarios(origen, destino);
                }
            }
        };
    }
}