// Archivo: src/controlador/ExploradorProyectosControlador.java
package controlador;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Proyecto;
import persistencia.DesignacionDAO;
import persistencia.LookupDAO;
import persistencia.ProyectoDAO;
import util.SesionUsuario;
import vista.DialogoEditarProyecto;
import vista.ExploradorProyectosVista;
import vista.ProyectoDetalleVista;
import vista.TarjetaProyectoPanel;

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

public class ExploradorProyectosControlador implements ActionListener {
    private final ExploradorProyectosVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final DesignacionDAO designacionDAO;
    private List<Proyecto> todosLosProyectos;
    private final String modo;

    public ExploradorProyectosControlador(ExploradorProyectosVista vista, JFrame vistaAnterior, String modo) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.modo = modo;
        this.proyectoDAO = new ProyectoDAO();
        this.designacionDAO = new DesignacionDAO();

        if ("VER".equals(modo)) {
            int idUsuarioLogueado = SesionUsuario.getUsuarioLogueado().getIdUsuario();
            List<String> roles = designacionDAO.listarNombresJerarquiaPorUsuario(idUsuarioLogueado);
            if (roles.contains("Project Manager") || roles.contains("Scrum Master")) {
                this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
            } else {
                this.todosLosProyectos = proyectoDAO.listarProyectosPorUsuario(idUsuarioLogueado);
            }
        } else { // Para EDITAR y EDITAR_TAREA, necesitamos todos los proyectos
            this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
        }

        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getComboFiltroPrioridad().addActionListener(this);
        this.vista.getTxtBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
        });
        this.vista.getBtnVistaLista().addActionListener(this);
        this.vista.getBtnVistaKanban().addActionListener(this);

        filtrarYMostrarProyectos();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
        } else {
            filtrarYMostrarProyectos();
        }
    }

    private void filtrarYMostrarProyectos() {
        String textoBusqueda = vista.getTxtBusqueda().getText().toLowerCase();
        Prioridad prioridadSeleccionada = (Prioridad) vista.getComboFiltroPrioridad().getSelectedItem();
        String nombrePrioridadFiltro = (prioridadSeleccionada != null && prioridadSeleccionada.getIdPrioridad() != 0)
                ? prioridadSeleccionada.getNombrePrioridad() : "";

        List<Proyecto> proyectosFiltrados = todosLosProyectos.stream()
                .filter(p -> p.getNombreProyecto().toLowerCase().contains(textoBusqueda))
                .filter(p -> nombrePrioridadFiltro.isEmpty() || p.getNombrePrioridad().equalsIgnoreCase(nombrePrioridadFiltro))
                .collect(Collectors.toList());

        if (vista.getBtnVistaLista().isSelected()) {
            mostrarProyectosEnLista(proyectosFiltrados);
        } else {
            mostrarProyectosEnKanban(proyectosFiltrados);
        }
    }

    private void mostrarProyectosEnLista(List<Proyecto> proyectos) {
        CardLayout cl = (CardLayout)(vista.getPanelContenedorPrincipal().getLayout());
        cl.show(vista.getPanelContenedorPrincipal(), "Lista");
        JPanel panelContenido = vista.getPanelContenidoLista();
        panelContenido.removeAll();
        for (Proyecto proyecto : proyectos) {
            panelContenido.add(crearTarjeta(proyecto));
            panelContenido.add(Box.createVerticalStrut(10));
        }
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarProyectosEnKanban(List<Proyecto> proyectos) {
        CardLayout cl = (CardLayout)(vista.getPanelContenedorPrincipal().getLayout());
        cl.show(vista.getPanelContenedorPrincipal(), "Kanban");
        Map<String, JPanel> columnas = vista.getColumnasKanban();
        columnas.values().forEach(JPanel::removeAll);
        for (Proyecto proyecto : proyectos) {
            String estado = proyecto.getNombreEstado().toUpperCase();
            if (columnas.containsKey(estado)) {
                columnas.get(estado).add(crearTarjeta(proyecto));
                columnas.get(estado).add(Box.createVerticalStrut(10));
            }
        }
        columnas.values().forEach(JPanel::repaint);
    }

    private TarjetaProyectoPanel crearTarjeta(Proyecto proyecto) {
        boolean esEditableProyecto = "EDITAR".equals(modo);
        TarjetaProyectoPanel tarjeta = new TarjetaProyectoPanel(proyecto, esEditableProyecto);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tarjeta.getBtnEditar() != null && tarjeta.getBtnEditar().getBounds().contains(e.getPoint())) {
                    return;
                }

                switch (modo) {
                    case "EDITAR":
                        abrirDialogoEdicionProyecto(proyecto);
                        break;
                    case "VER":
                        abrirVistaDeDetalle(proyecto, false);
                        break;
                    case "EDITAR_TAREA":
                        abrirVistaDeDetalle(proyecto, true);
                        break;
                }
            }
        });

        if (esEditableProyecto && tarjeta.getBtnEditar() != null) {
            tarjeta.getBtnEditar().addActionListener(e -> abrirDialogoEdicionProyecto(proyecto));
        }
        return tarjeta;
    }

    private void abrirDialogoEdicionProyecto(Proyecto proyecto) {
        LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
        List<Prioridad> prioridades = prioridadLookup.listarTodos("prioridades", rs -> new Prioridad(rs.getInt("IDPrioridad"), rs.getString("NombrePrioridad")));

        LookupDAO<Estado> estadoLookup = new LookupDAO<>();
        List<Estado> estados = estadoLookup.listarTodos("estados", rs -> new Estado(rs.getInt("IDEstado"), rs.getString("NombreEstado")));

        DialogoEditarProyecto dialogo = new DialogoEditarProyecto(vista, proyecto, prioridades, estados);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Proyecto proyectoActualizado = dialogo.getProyectoActualizado();
            boolean exito = proyectoDAO.actualizar(proyectoActualizado);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Proyecto actualizado correctamente.");
                this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
                filtrarYMostrarProyectos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el proyecto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirVistaDeDetalle(Proyecto proyecto, boolean modoEdicionTareas) {
        vista.setVisible(false);
        LookupDAO<Prioridad> prioridadLookup = new LookupDAO<>();
        List<Prioridad> prioridadesDisponibles = prioridadLookup.listarTodos("prioridades", rs -> new Prioridad(rs.getInt("IDPrioridad"), rs.getString("NombrePrioridad")));

        ProyectoDetalleVista proyectoDetalleVista = new ProyectoDetalleVista(proyecto, prioridadesDisponibles);
        ProyectoDetalleControlador detalleControlador = new ProyectoDetalleControlador(proyectoDetalleVista, vista, modoEdicionTareas);
        detalleControlador.iniciar();
    }
}