package controlador;

import modelo.Prioridad;
import persistencia.PrioridadDAO;
import persistencia.UsuarioDAO;
import util.SesionUsuario;
import vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuPrincipalControlador implements ActionListener {

    private final MenuPrincipalVista vista;

    public MenuPrincipalControlador(MenuPrincipalVista vista) {
        this.vista = vista;
        this.vista.getBtnGestionUsuarios().addActionListener(this);
        this.vista.getBtnGestionProyectos().addActionListener(this);
        this.vista.getBtnMisProyectos().addActionListener(this);
        this.vista.getBtnGestionTareas().addActionListener(this);
        this.vista.getBtnReportes().addActionListener(this);
        this.vista.getBtnCerrarSesion().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnGestionUsuarios()) {
            vista.setVisible(false);
            GestionUsuariosMenuVista gestionMenu = new GestionUsuariosMenuVista();
            GestionUsuariosMenuControlador gestionControlador = new GestionUsuariosMenuControlador(gestionMenu, vista);
            gestionControlador.iniciar();
        } else if (e.getSource() == vista.getBtnGestionProyectos()) {
            vista.setVisible(false);
            GestionProyectosMenuVista gestionProyectosMenu = new GestionProyectosMenuVista();
            GestionProyectosMenuControlador gestionProyectosControlador = new GestionProyectosMenuControlador(gestionProyectosMenu, vista);
            gestionProyectosControlador.iniciar();
        } else if (e.getSource() == vista.getBtnMisProyectos()) {
            vista.setVisible(false);
            PrioridadDAO prioridadDAO = new PrioridadDAO();
            List<Prioridad> prioridades = prioridadDAO.listarTodos();
            MisProyectosVista misProyectosVista = new MisProyectosVista(prioridades);
            MisProyectosControlador misProyectosControlador = new MisProyectosControlador(misProyectosVista, vista);
            misProyectosControlador.iniciar();
        } else if (e.getSource() == vista.getBtnGestionTareas()) {
            vista.setVisible(false);
            GestionTareasMenuVista tareasMenu = new GestionTareasMenuVista();
            GestionTareasMenuControlador tareasControlador = new GestionTareasMenuControlador(tareasMenu, vista);
            tareasControlador.iniciar();
        } else if (e.getSource() == vista.getBtnReportes()) {
            JOptionPane.showMessageDialog(vista, "Funcionalidad de Reportes (próximamente)");
        } else if (e.getSource() == vista.getBtnCerrarSesion()) {
            cerrarSesion();
        }
    }

    private void cerrarSesion() {
        SesionUsuario.setUsuarioLogueado(null);
        vista.dispose();
        LoginVista loginVista = new LoginVista();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        LoginControlador loginControlador = new LoginControlador(loginVista, usuarioDAO);
        loginControlador.iniciar();
    }
}