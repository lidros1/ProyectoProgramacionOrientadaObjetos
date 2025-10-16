package controlador;

import modelo.Comentario;
import modelo.Tarea;
import modelo.Usuario;
import persistencia.ComentarioDAO;
import persistencia.DesignacionDAO;
import persistencia.TareaDAO;
import util.SesionUsuario;
import vista.TareaDetalleVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class TareaDetalleControlador implements ActionListener {
    private final TareaDetalleVista vista;
    private final JFrame vistaAnterior;
    private final TareaDAO tareaDAO;
    private final ComentarioDAO comentarioDAO;
    private final DesignacionDAO designacionDAO;
    private final int idTarea;

    public TareaDetalleControlador(TareaDetalleVista vista, JFrame vistaAnterior, int idTarea) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.idTarea = idTarea;
        this.tareaDAO = new TareaDAO();
        this.comentarioDAO = new ComentarioDAO();
        this.designacionDAO = new DesignacionDAO();

        this.vista.getBtnVolverATareas().addActionListener(this);
        this.vista.getBtnPublicarComentario().addActionListener(this); // <-- LISTENER AÑADIDO
        cargarDetallesTarea();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolverATareas()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        } else if (e.getSource() == vista.getBtnPublicarComentario()) {
            publicarComentario(); // <-- LLAMADA AL NUEVO MÉTODO
        }
    }

    private void cargarDetallesTarea() {
        Tarea tarea = tareaDAO.obtenerTareaCompletaPorId(idTarea);
        if (tarea != null) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            vista.getLblNombreTarea().setText(tarea.getNombreTarea());
            vista.getLblPorcentajeAvance().setText(tarea.getPorcentajeAvance() + "%");
            vista.getLblFechaInicio().setText(tarea.getFechaInicio() != null ? sdfDate.format(tarea.getFechaInicio()) : "N/A");
            vista.getLblFechaFinEstimada().setText(tarea.getFechaFinalEstimada() != null ? sdfDate.format(tarea.getFechaFinalEstimada()) : "N/A");
            vista.getLblEstado().setText(tarea.getNombreEstado());
            vista.getLblPrioridad().setText(tarea.getNombrePrioridad());

            // Cargar usuarios y comentarios
            refrescarComentarios();
            refrescarUsuariosDesignados();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo cargar la información de la tarea.", "Error", JOptionPane.ERROR_MESSAGE);
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void publicarComentario() {
        String contenido = vista.getAreaNuevoComentario().getText().trim();
        if (contenido.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El comentario no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogueado = SesionUsuario.getUsuarioLogueado();
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(vista, "Error de sesión. No se puede publicar el comentario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setIdTarea(this.idTarea);
        nuevoComentario.setIdUsuario(usuarioLogueado.getIdUsuario());
        nuevoComentario.setContenido(contenido);

        if (comentarioDAO.insertar(nuevoComentario)) {
            vista.getAreaNuevoComentario().setText("");
            refrescarComentarios();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar el comentario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarUsuariosDesignados() {
        List<Usuario> usuariosTarea = designacionDAO.listarUsuariosDesignadosATarea(idTarea);
        vista.getListModelUsuariosTarea().clear();
        usuariosTarea.forEach(vista.getListModelUsuariosTarea()::addElement);
    }

    private void refrescarComentarios() {
        List<Comentario> comentarios = comentarioDAO.listarComentariosPorTarea(idTarea);
        vista.getListModelComentarios().clear();
        comentarios.forEach(vista.getListModelComentarios()::addElement);
    }
}