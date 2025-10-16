package controlador;

import modelo.AreaSistema;
import modelo.Funcion;
import persistencia.AreaSistemaDAO;
import persistencia.FuncionDAO;
import vista.CrearUsuarioVista;
import vista.EditarUsuarioVista; // IMPORTAR
import vista.GestionUsuariosMenuVista;
import vista.ListarUsuarioVista;
import vista.MenuPrincipalVista;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionUsuariosMenuControlador implements ActionListener {
    private final GestionUsuariosMenuVista vista;
    private final JFrame vistaAnterior;

    public GestionUsuariosMenuControlador(GestionUsuariosMenuVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;

        this.vista.getBtnCrearUsuario().addActionListener(this);
        this.vista.getBtnListarUsuario().addActionListener(this);
        this.vista.getBtnEditarUsuario().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnCrearUsuario()) {
            vista.setVisible(false);
            AreaSistemaDAO areaDAO = new AreaSistemaDAO();
            FuncionDAO funcionDAO = new FuncionDAO();
            List<AreaSistema> areas = areaDAO.listarTodas();
            List<Funcion> funciones = funcionDAO.listarTodas();
            CrearUsuarioVista crearVista = new CrearUsuarioVista(areas, funciones);
            CrearUsuarioControlador crearControlador = new CrearUsuarioControlador(crearVista, vista);
            crearControlador.iniciar();

        } else if (e.getSource() == vista.getBtnListarUsuario()) {
            vista.setVisible(false);
            ListarUsuarioVista listarVista = new ListarUsuarioVista();
            ListarUsuarioControlador listarControlador = new ListarUsuarioControlador(listarVista, vista);
            listarControlador.iniciar();

        } else if (e.getSource() == vista.getBtnEditarUsuario()) {
            // --- MODIFICACIÓN AQUÍ ---
            vista.setVisible(false);
            AreaSistemaDAO areaDAO = new AreaSistemaDAO();
            FuncionDAO funcionDAO = new FuncionDAO();
            List<AreaSistema> areas = areaDAO.listarTodas();
            List<Funcion> funciones = funcionDAO.listarTodas();
            EditarUsuarioVista editarVista = new EditarUsuarioVista(areas, funciones);
            EditarUsuarioControlador editarControlador = new EditarUsuarioControlador(editarVista, vista);
            editarControlador.iniciar();
            // -------------------------

        } else if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        }
    }
}