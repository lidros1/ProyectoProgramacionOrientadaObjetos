package controlador;

import modelo.Prioridad;
import persistencia.PrioridadDAO;
import vista.*; // Importar todas las vistas necesarias

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionProyectosMenuControlador implements ActionListener {
    private final GestionProyectosMenuVista vista;
    private final JFrame vistaAnterior;

    public GestionProyectosMenuControlador(GestionProyectosMenuVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.vista.getBtnCrearProyecto().addActionListener(this);
        this.vista.getBtnListarProyecto().addActionListener(this);
        this.vista.getBtnEditarProyecto().addActionListener(this);
        this.vista.getBtnDesignarProyecto().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrearProyecto()) {
            vista.setVisible(false);
            PrioridadDAO prioridadDAO = new PrioridadDAO();
            List<Prioridad> prioridades = prioridadDAO.listarTodos();
            CrearProyectoVista crearProyectoVista = new CrearProyectoVista(prioridades);
            CrearProyectoControlador crearProyectoControlador = new CrearProyectoControlador(crearProyectoVista, vista);
            crearProyectoControlador.iniciar();
        } else if (e.getSource() == vista.getBtnListarProyecto()) {
            vista.setVisible(false);
            PrioridadDAO prioridadDAO = new PrioridadDAO();
            List<Prioridad> prioridades = prioridadDAO.listarTodos();
            ListarProyectoVista listarVista = new ListarProyectoVista(prioridades);
            ListarProyectoControlador listarControlador = new ListarProyectoControlador(listarVista, vista);
            listarControlador.iniciar();
        } else if (e.getSource() == vista.getBtnEditarProyecto()) {
            vista.setVisible(false);
            PrioridadDAO prioridadDAO = new PrioridadDAO();
            List<Prioridad> prioridades = prioridadDAO.listarTodos();
            EditarProyectoVista editarVista = new EditarProyectoVista(prioridades);
            EditarProyectoControlador editarControlador = new EditarProyectoControlador(editarVista, vista);
            editarControlador.iniciar();
        } else if (e.getSource() == vista.getBtnDesignarProyecto()) {
            // --- MODIFICACIÓN AQUÍ ---
            vista.setVisible(false);
            DesignarUsuariosProyectoVista designarVista = new DesignarUsuariosProyectoVista();
            DesignarUsuariosProyectoControlador designarControlador = new DesignarUsuariosProyectoControlador(designarVista, vista);
            designarControlador.iniciar();
            // -------------------------
        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }
}