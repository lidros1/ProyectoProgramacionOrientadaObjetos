package controlador;

import modelo.Prioridad;
import persistencia.LookupDAO;
import vista.*;
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
        vista.setVisible(false);

        // --- USO DEL LOOKUP DAO ---
        LookupDAO<Prioridad> lookupDAO = new LookupDAO<>();
        List<Prioridad> prioridades = lookupDAO.listarTodos("prioridades", rs -> {
            Prioridad p = new Prioridad();
            p.setIdPrioridad(rs.getInt("IDPrioridad"));
            p.setNombrePrioridad(rs.getString("NombrePrioridad"));
            p.setDescripcionPrioridad(rs.getString("DescripcionPrioridad"));
            return p;
        });
        // --------------------------------

        if (e.getSource() == vista.getBtnCrearProyecto()) {
            CrearProyectoVista crearProyectoVista = new CrearProyectoVista(prioridades);
            CrearProyectoControlador crearProyectoControlador = new CrearProyectoControlador(crearProyectoVista, vista);
            crearProyectoControlador.iniciar();

        } else if (e.getSource() == vista.getBtnListarProyecto()) {
            ExploradorProyectosVista exploradorVista = new ExploradorProyectosVista(prioridades);
            exploradorVista.setTitle("Listar Proyectos");
            ExploradorProyectosControlador exploradorControlador = new ExploradorProyectosControlador(exploradorVista, vista, "VER");
            exploradorControlador.iniciar();

        } else if (e.getSource() == vista.getBtnEditarProyecto()) {
            ExploradorProyectosVista exploradorVista = new ExploradorProyectosVista(prioridades);
            exploradorVista.setTitle("Editar Proyectos");
            ExploradorProyectosControlador exploradorControlador = new ExploradorProyectosControlador(exploradorVista, vista, "EDITAR");
            exploradorControlador.iniciar();

        } else if (e.getSource() == vista.getBtnDesignarProyecto()) {
            DesignarUsuariosProyectoVista designarVista = new DesignarUsuariosProyectoVista();
            DesignarUsuariosProyectoControlador designarControlador = new DesignarUsuariosProyectoControlador(designarVista, vista);
            designarControlador.iniciar();

        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }
}