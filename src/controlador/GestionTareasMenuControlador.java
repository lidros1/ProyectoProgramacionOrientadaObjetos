package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import modelo.Usuario;
import persistencia.LookupDAO;
import persistencia.ProyectoDAO;
import persistencia.UsuarioDAO;
import vista.AsignarTareaVista;
import vista.CrearTareaVista;
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
        this.vista.getBtnAsignarTarea().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrearTarea()) {
            vista.setVisible(false);
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            // --- USO DEL NUEVO LOOKUP DAO ---
            LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
            List<Prioridad> prioridades = prioridadLookup.listarTodos("prioridades", rs -> {
                Prioridad p = new Prioridad();
                p.setIdPrioridad(rs.getInt("IDPrioridad"));
                p.setNombrePrioridad(rs.getString("NombrePrioridad"));
                return p;
            });
            // --------------------------------

            List<Proyecto> proyectos = proyectoDAO.listarTodosLosProyectos();
            List<Usuario> usuarios = usuarioDAO.listarTodos();

            CrearTareaVista crearTareaVista = new CrearTareaVista(prioridades, usuarios);
            CrearTareaControlador crearTareaControlador = new CrearTareaControlador(crearTareaVista, vista, proyectos);
            crearTareaControlador.iniciar();

        } else if (e.getSource() == vista.getBtnListarTarea()) {
            vista.setVisible(false);
            ProyectoDAO proyectoDAO = new ProyectoDAO();

            // --- USO DEL NUEVO LOOKUP DAO ---
            LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
            List<Prioridad> prioridades = prioridadLookup.listarTodos("prioridades", rs -> {
                Prioridad p = new Prioridad();
                p.setIdPrioridad(rs.getInt("IDPrioridad"));
                p.setNombrePrioridad(rs.getString("NombrePrioridad"));
                return p;
            });
            // --------------------------------

            List<Proyecto> proyectos = proyectoDAO.listarTodosLosProyectos();

            ListarTareaVista listarVista = new ListarTareaVista(prioridades, proyectos);
            ListarTareaControlador listarControlador = new ListarTareaControlador(listarVista, vista);
            listarControlador.iniciar();

        } else if (e.getSource() == vista.getBtnAsignarTarea()) {
            vista.setVisible(false);
            AsignarTareaVista asignarVista = new AsignarTareaVista();
            AsignarTareaControlador asignarControlador = new AsignarTareaControlador(asignarVista, vista);
            asignarControlador.iniciar();

        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }
}