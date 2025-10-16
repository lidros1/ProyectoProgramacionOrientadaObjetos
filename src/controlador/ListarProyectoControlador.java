package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import persistencia.DesignacionDAO;
import persistencia.PrioridadDAO;
import persistencia.ProyectoDAO;
import util.SesionUsuario;
import vista.ListarProyectoVista;
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

public class ListarProyectoControlador implements ActionListener {
    private final ListarProyectoVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final PrioridadDAO prioridadDAO;
    private final DesignacionDAO designacionDAO;
    private List<Proyecto> todosLosProyectos;

    public ListarProyectoControlador(ListarProyectoVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.prioridadDAO = new PrioridadDAO();
        this.designacionDAO = new DesignacionDAO();

        int idUsuarioLogueado = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        List<String> roles = designacionDAO.listarNombresJerarquiaPorUsuario(idUsuarioLogueado);

        if (roles.contains("Project Manager") || roles.contains("Scrum Master")) {
            this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();
        } else {
            this.todosLosProyectos = proyectoDAO.listarProyectosPorUsuario(idUsuarioLogueado);
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
        String nombrePrioridadFiltro = "";
        if (prioridadSeleccionada != null && prioridadSeleccionada.getIdPrioridad() != 0) {
            nombrePrioridadFiltro = prioridadSeleccionada.getNombrePrioridad();
        }

        String finalNombrePrioridadFiltro = nombrePrioridadFiltro;
        List<Proyecto> proyectosFiltrados = todosLosProyectos.stream()
                .filter(p -> p.getNombreProyecto().toLowerCase().contains(textoBusqueda))
                .filter(p -> finalNombrePrioridadFiltro.isEmpty() || p.getNombrePrioridad().equalsIgnoreCase(finalNombrePrioridadFiltro))
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
        columnas.values().forEach(c -> {
            c.revalidate();
            c.repaint();
        });
    }

    private TarjetaProyectoPanel crearTarjeta(Proyecto proyecto) {
        TarjetaProyectoPanel tarjeta = new TarjetaProyectoPanel(proyecto);
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                List<Prioridad> prioridadesDisponibles = prioridadDAO.listarTodos();
                ProyectoDetalleVista proyectoDetalleVista = new ProyectoDetalleVista(proyecto, prioridadesDisponibles);
                // <-- CORRECCIÓN AQUÍ: Se añade 'false' como tercer argumento -->
                ProyectoDetalleControlador detalleControlador = new ProyectoDetalleControlador(proyectoDetalleVista, vista, false);
                detalleControlador.iniciar();
            }
        });
        return tarjeta;
    }
}