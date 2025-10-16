package controlador;

import modelo.*;
import persistencia.*;
import util.SesionUsuario;
import vista.DialogoActualizarTarea;
import vista.DialogoEditarTarea;
import vista.ProyectoDetalleVista;
import vista.TarjetaTareaPanel;
import vista.TareaDetalleVista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProyectoDetalleControlador implements ActionListener {
    private final ProyectoDetalleVista vista;
    private final JFrame vistaAnterior;
    private final TareaDAO tareaDAO;
    private final DesignacionDAO designacionDAO;
    private final PrioridadDAO prioridadDAO;
    private final EstadoDAO estadoDAO;
    private final UsuarioDAO usuarioDAO;
    private final ComentarioDAO comentarioDAO;
    private final Proyecto proyectoActual;
    private List<Tarea> todasLasTareasDelProyecto;
    private final boolean modoEdicionCompleta;

    public ProyectoDetalleControlador(ProyectoDetalleVista vista, JFrame vistaAnterior, boolean modoEdicionCompleta) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.modoEdicionCompleta = modoEdicionCompleta;
        this.tareaDAO = new TareaDAO();
        this.designacionDAO = new DesignacionDAO();
        this.prioridadDAO = new PrioridadDAO();
        this.estadoDAO = new EstadoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.comentarioDAO = new ComentarioDAO();
        this.proyectoActual = vista.getProyecto();

        int idUsuarioLogueado = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        List<String> roles = designacionDAO.listarNombresJerarquiaPorUsuario(idUsuarioLogueado);
        boolean esUsuarioGestor = roles.contains("Project Manager") || roles.contains("Scrum Master");

        if (esUsuarioGestor) {
            this.todasLasTareasDelProyecto = tareaDAO.listarTareasPorProyecto(proyectoActual.getIdProyecto());
        } else {
            this.todasLasTareasDelProyecto = tareaDAO.listarTareasPorProyectoYUsuario(proyectoActual.getIdProyecto(), idUsuarioLogueado);
        }

        // Cargar usuarios del proyecto en la vista principal
        cargarUsuariosDesignadosAProyecto();

        this.vista.getBtnVolverAProyectos().addActionListener(this);
        this.vista.getComboFiltroPrioridadTareas().addActionListener(this);
        this.vista.getTxtBusquedaTareas().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
        });
        this.vista.getBtnVistaListaTareas().addActionListener(this);
        this.vista.getBtnVistaKanbanTareas().addActionListener(this);

        filtrarYMostrarTareas();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolverAProyectos()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        } else {
            filtrarYMostrarTareas();
        }
    }

    private void cargarUsuariosDesignadosAProyecto() {
        List<Usuario> usuarios = designacionDAO.listarUsuariosDesignadosAProyecto(proyectoActual.getIdProyecto());
        proyectoActual.setUsuariosDesignados(usuarios);
        vista.getListModelUsuariosProyecto().clear();
        for (Usuario u : usuarios) {
            vista.getListModelUsuariosProyecto().addElement(u);
        }
    }

    private void filtrarYMostrarTareas() {
        String textoBusqueda = vista.getTxtBusquedaTareas().getText().toLowerCase();
        Prioridad prioridadSeleccionada = (Prioridad) vista.getComboFiltroPrioridadTareas().getSelectedItem();
        String nombrePrioridadFiltro = "";
        if (prioridadSeleccionada != null && prioridadSeleccionada.getIdPrioridad() != 0) {
            nombrePrioridadFiltro = prioridadSeleccionada.getNombrePrioridad();
        }

        String finalNombrePrioridadFiltro = nombrePrioridadFiltro;
        List<Tarea> tareasFiltradas = todasLasTareasDelProyecto.stream()
                .filter(t -> t.getNombreTarea().toLowerCase().contains(textoBusqueda))
                .filter(t -> finalNombrePrioridadFiltro.isEmpty() || t.getNombrePrioridad().equalsIgnoreCase(finalNombrePrioridadFiltro))
                .collect(Collectors.toList());

        if (vista.getBtnVistaListaTareas().isSelected()) {
            mostrarTareasEnLista(tareasFiltradas);
        } else {
            mostrarTareasEnKanban(tareasFiltradas);
        }
    }

    private void mostrarTareasEnLista(List<Tarea> tareas) {
        CardLayout cl = (CardLayout)(vista.getPanelContenedorPrincipalTareas().getLayout());
        cl.show(vista.getPanelContenedorPrincipalTareas(), "ListaTareas");
        JPanel panelContenido = vista.getPanelContenidoListaTareas();
        panelContenido.removeAll();
        for (Tarea tarea : tareas) {
            panelContenido.add(crearTarjetaTarea(tarea));
            panelContenido.add(Box.createVerticalStrut(10));
        }
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarTareasEnKanban(List<Tarea> tareas) {
        CardLayout cl = (CardLayout)(vista.getPanelContenedorPrincipalTareas().getLayout());
        cl.show(vista.getPanelContenedorPrincipalTareas(), "KanbanTareas");
        Map<String, JPanel> columnas = vista.getColumnasKanbanTareas();
        columnas.values().forEach(JPanel::removeAll);
        for (Tarea tarea : tareas) {
            String estado = tarea.getNombreEstado().toUpperCase();
            if (columnas.containsKey(estado)) {
                columnas.get(estado).add(crearTarjetaTarea(tarea));
                columnas.get(estado).add(Box.createVerticalStrut(10));
            }
        }
        columnas.values().forEach(c -> {
            c.revalidate();
            c.repaint();
        });
    }

    private TarjetaTareaPanel crearTarjetaTarea(Tarea tarea) {
        TarjetaTareaPanel tarjeta = new TarjetaTareaPanel(tarea);
        tarjeta.setToolTipText("Clic para interactuar con la tarea");
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modoEdicionCompleta) {
                    abrirDialogoEdicionTarea(tarea);
                } else {
                    abrirDialogoActualizarTarea(tarea);
                }
            }
        });
        return tarjeta;
    }

    private void abrirDialogoEdicionTarea(Tarea tarea) {
        List<Prioridad> prioridades = prioridadDAO.listarTodos();
        List<Estado> estados = estadoDAO.listarTodos();
        List<Usuario> todosLosUsuarios = usuarioDAO.listarTodos();
        tarea.setUsuariosDesignados(designacionDAO.listarUsuariosDesignadosATarea(tarea.getIdTarea()));

        DialogoEditarTarea dialogo = new DialogoEditarTarea(vista, tarea, prioridades, estados, todosLosUsuarios);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Tarea tareaActualizada = dialogo.getTareaActualizada();
            boolean exitoTarea = tareaDAO.actualizar(tareaActualizada);
            boolean exitoDesignacion = designacionDAO.eliminarDesignacionesPorTarea(tareaActualizada.getIdTarea());
            if (exitoDesignacion && !tareaActualizada.getUsuariosDesignados().isEmpty()) {
                exitoDesignacion = designacionDAO.insertarDesignacionesTarea(tareaActualizada.getIdTarea(), tareaActualizada.getUsuariosDesignados());
            }

            if (exitoTarea && exitoDesignacion) {
                JOptionPane.showMessageDialog(vista, "Tarea actualizada correctamente.");
                refrescarListaDeTareas();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar la tarea.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirDialogoActualizarTarea(Tarea tarea) {
        List<Estado> estados = estadoDAO.listarTodos();
        DialogoActualizarTarea dialogo = new DialogoActualizarTarea(vista, tarea, estados);

        dialogo.getBtnPublicarComentario().addActionListener(e -> {
            String contenido = dialogo.getAreaNuevoComentario().getText().trim();
            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "El comentario no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Comentario nuevoComentario = new Comentario();
            nuevoComentario.setIdTarea(tarea.getIdTarea());
            nuevoComentario.setIdUsuario(SesionUsuario.getUsuarioLogueado().getIdUsuario());
            nuevoComentario.setContenido(contenido);

            if (comentarioDAO.insertar(nuevoComentario)) {
                dialogo.getAreaNuevoComentario().setText("");
                List<Comentario> comentarios = comentarioDAO.listarComentariosPorTarea(tarea.getIdTarea());
                dialogo.getListModelComentarios().clear();
                comentarios.forEach(dialogo.getListModelComentarios()::addElement);
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al publicar el comentario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        List<Comentario> comentarios = comentarioDAO.listarComentariosPorTarea(tarea.getIdTarea());
        comentarios.forEach(dialogo.getListModelComentarios()::addElement);

        List<Usuario> usuariosDesignados = designacionDAO.listarUsuariosDesignadosATarea(tarea.getIdTarea());
        usuariosDesignados.forEach(dialogo.getListModelUsuarios()::addElement);

        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Estado nuevoEstado = dialogo.getComboEstado().getSelectedItem() instanceof Estado ? (Estado) dialogo.getComboEstado().getSelectedItem() : null;
            if (nuevoEstado != null && nuevoEstado.getIdEstado() != tarea.getIdEstado()) {
                if (tareaDAO.actualizarEstado(tarea.getIdTarea(), nuevoEstado.getIdEstado())) {
                    JOptionPane.showMessageDialog(vista, "Estado de la tarea actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    refrescarListaDeTareas();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refrescarListaDeTareas() {
        int idUsuarioLogueado = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        List<String> roles = designacionDAO.listarNombresJerarquiaPorUsuario(idUsuarioLogueado);
        boolean esGestor = roles.contains("Project Manager") || roles.contains("Scrum Master");

        if (esGestor) {
            this.todasLasTareasDelProyecto = tareaDAO.listarTareasPorProyecto(proyectoActual.getIdProyecto());
        } else {
            this.todasLasTareasDelProyecto = tareaDAO.listarTareasPorProyectoYUsuario(proyectoActual.getIdProyecto(), idUsuarioLogueado);
        }
        filtrarYMostrarTareas();
    }
}