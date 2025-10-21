// Archivo: src/controlador/GestionTareasMenuControlador.java
package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import persistencia.LookupDAO;
import persistencia.ProyectoDAO;
import persistencia.UsuarioDAO;
import vista.AsignarTareaVista;
import vista.CrearTareaVista;
import vista.ExploradorProyectosVista;
import vista.GestionTareasMenuVista;
import vista.ListarTareaVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionTareasMenuControlador implements ActionListener {
    private final GestionTareasMenuVista vista;
    private final JFrame vistaAnterior;

    public GestionTareasMenuControlador(GestionTareasMenuVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;

        this.vista.getBtnCrearTarea().addActionListener(this);
        this.vista.getBtnListarTarea().addActionListener(this);
        this.vista.getBtnEditarTarea().addActionListener(this);
        this.vista.getBtnAsignarTarea().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrearTarea()) {
            abrirCrearTarea();
        } else if (e.getSource() == vista.getBtnListarTarea()) {
            abrirListarTarea();
        } else if (e.getSource() == vista.getBtnEditarTarea()) {
            abrirEditarTarea();
        } else if (e.getSource() == vista.getBtnAsignarTarea()) {
            abrirAsignarTarea();
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }

    private void abrirEditarTarea() {
        vista.setVisible(false);
        LookupDAO<Prioridad> lookupDAO = new LookupDAO<>();
        List<Prioridad> prioridades = lookupDAO.listarTodos("prioridades", rs -> new Prioridad(rs.getInt("IDPrioridad"), rs.getString("NombrePrioridad")));

        ExploradorProyectosVista exploradorVista = new ExploradorProyectosVista(prioridades);
        exploradorVista.setTitle("Editar Tarea (Paso 1: Seleccionar Proyecto)");
        ExploradorProyectosControlador exploradorControlador = new ExploradorProyectosControlador(exploradorVista, vista, "EDITAR_TAREA");
        exploradorControlador.iniciar();
    }

    private void abrirCrearTarea() {
        vista.setVisible(false);
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
        List<Prioridad> prioridades = prioridadLookup.listarTodos("prioridades", rs -> new Prioridad(rs.getInt("IDPrioridad"), rs.getString("NombrePrioridad")));
        List<Proyecto> proyectos = proyectoDAO.listarTodosLosProyectos();
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        CrearTareaVista crearTareaVista = new CrearTareaVista(prioridades, usuarios);
        CrearTareaControlador crearTareaControlador = new CrearTareaControlador(crearTareaVista, vista, proyectos);
        crearTareaControlador.iniciar();
    }

    private void abrirListarTarea() {
        vista.setVisible(false);
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
        List<Prioridad> prioridades = prioridadLookup.listarTodos("prioridades", rs -> new Prioridad(rs.getInt("IDPrioridad"), rs.getString("NombrePrioridad")));
        List<Proyecto> proyectos = proyectoDAO.listarTodosLosProyectos();

        ListarTareaVista listarVista = new ListarTareaVista(prioridades, proyectos);
        ListarTareaControlador listarControlador = new ListarTareaControlador(listarVista, vista);
        listarControlador.iniciar();
    }

    private void abrirAsignarTarea() {
        vista.setVisible(false);
        AsignarTareaVista asignarVista = new AsignarTareaVista();
        AsignarTareaControlador asignarControlador = new AsignarTareaControlador(asignarVista, vista);
        asignarControlador.iniciar();
    }
}