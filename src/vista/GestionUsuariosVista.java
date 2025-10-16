// Archivo: src/vista/GestionUsuariosVista.java
package vista;

import javax.swing.*;
import java.awt.*;

// Esta es la VISTA. No contiene lógica de negocio, solo componentes gráficos.
public class GestionUsuariosVista extends JFrame {
    private JTable tablaUsuarios;
    private JButton botonNuevo;
    private JButton botonEditar;
    private JButton botonEliminar;
    private UsuarioTableModel tableModel;

    public GestionUsuariosVista() {
        setTitle("Gestión de Usuarios");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Tabla
        tablaUsuarios = new JTable();
        panelPrincipal.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        botonNuevo = new JButton("Nuevo");
        botonEditar = new JButton("Editar");
        botonEliminar = new JButton("Eliminar");

        panelBotones.add(botonNuevo);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    // Getters para que el Controlador pueda acceder a los componentes
    public JTable getTablaUsuarios() {
        return tablaUsuarios;
    }

    public JButton getBotonNuevo() {
        return botonNuevo;
    }

    public JButton getBotonEditar() {
        return botonEditar;
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    // Método para establecer el modelo de la tabla
    public void setTableModel(UsuarioTableModel model) {
        this.tableModel = model;
        this.tablaUsuarios.setModel(model);
    }
}