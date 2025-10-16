package controlador;

import modelo.Prioridad;
import modelo.Proyecto;
import persistencia.PrioridadDAO;
import persistencia.ProyectoDAO;
import util.SesionUsuario;
import vista.MisProyectosVista;
import vista.ProyectoDetalleVista;
import vista.TarjetaProyectoPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MisProyectosControlador implements ActionListener {
    private final MisProyectosVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final PrioridadDAO prioridadDAO;
    private List<Proyecto> proyectosDelUsuario;

    public MisProyectosControlador(MisProyectosVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.prioridadDAO = new PrioridadDAO();

        int idUsuarioLogueado = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        this.proyectosDelUsuario = proyectoDAO.listarProyectosPorUsuario(idUsuarioLogueado);

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getComboFiltroPrioridad().addActionListener(this);
        this.vista.getTxtBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarYMostrarProyectos(); }
        });

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
        List<Proyecto> proyectosFiltrados = proyectosDelUsuario.stream()
                .filter(p -> p.getNombreProyecto().toLowerCase().contains(textoBusqueda))
                .filter(p -> finalNombrePrioridadFiltro.isEmpty() || p.getNombrePrioridad().equalsIgnoreCase(finalNombrePrioridadFiltro))
                .collect(Collectors.toList());

        mostrarProyectosEnKanban(proyectosFiltrados);
    }

    private void mostrarProyectosEnKanban(List<Proyecto> proyectos) {
        Map<String, JPanel> columnas = vista.getColumnasKanban();
        columnas.values().forEach(JPanel::removeAll);

        for (Proyecto proyecto : proyectos) {
            String estado = proyecto.getNombreEstado().toUpperCase();
            if (columnas.containsKey(estado)) {
                TarjetaProyectoPanel tarjeta = crearTarjeta(proyecto);
                columnas.get(estado).add(tarjeta);
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
                List<Prioridad> prioridades = prioridadDAO.listarTodos();
                ProyectoDetalleVista detalleVista = new ProyectoDetalleVista(proyecto, prioridades);
                // --- MODIFICACIÓN AQUÍ: Se pasa 'false' para el modo de edición ---
                ProyectoDetalleControlador detalleControlador = new ProyectoDetalleControlador(detalleVista, vista, false);
                detalleControlador.iniciar();
            }
        });
        return tarjeta;
    }
}