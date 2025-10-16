package controlador;

import modelo.Prioridad;
import modelo.Proyecto; // <-- IMPORTAR
import modelo.Tarea;
import persistencia.TareaDAO;
import vista.ListarTareaVista;
import vista.TarjetaTareaPanel;
import vista.TareaDetalleVista;

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

public class ListarTareaControlador implements ActionListener {
    private final ListarTareaVista vista;
    private final JFrame vistaAnterior;
    private final TareaDAO tareaDAO;
    private List<Tarea> todasLasTareas;

    public ListarTareaControlador(ListarTareaVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.tareaDAO = new TareaDAO();
        this.todasLasTareas = tareaDAO.listarTodasLasTareas();

        // Listeners
        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getComboFiltroProyecto().addActionListener(this); // <-- NUEVO LISTENER
        this.vista.getComboFiltroPrioridad().addActionListener(this);
        this.vista.getBtnVistaLista().addActionListener(this);
        this.vista.getBtnVistaKanban().addActionListener(this);
        this.vista.getTxtBusqueda().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarYMostrarTareas(); }
        });

        filtrarYMostrarTareas();
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
            // Cualquier otro ActionEvent (de los JComboBox o JToggleButton) dispara el filtro
            filtrarYMostrarTareas();
        }
    }

    private void filtrarYMostrarTareas() {
        // --- LÓGICA DE FILTROS ACTUALIZADA ---
        String textoBusqueda = vista.getTxtBusqueda().getText().toLowerCase();

        Proyecto proyectoSeleccionado = (Proyecto) vista.getComboFiltroProyecto().getSelectedItem();
        Prioridad prioridadSeleccionada = (Prioridad) vista.getComboFiltroPrioridad().getSelectedItem();

        String nombrePrioridadFiltro = "";
        if (prioridadSeleccionada != null && prioridadSeleccionada.getIdPrioridad() != 0) {
            nombrePrioridadFiltro = prioridadSeleccionada.getNombrePrioridad();
        }

        String finalNombrePrioridadFiltro = nombrePrioridadFiltro;
        List<Tarea> tareasFiltradas = todasLasTareas.stream()
                // 1. Filtrar por proyecto
                .filter(t -> proyectoSeleccionado == null || proyectoSeleccionado.getIdProyecto() == 0 || t.getIdProyecto() == proyectoSeleccionado.getIdProyecto())
                // 2. Filtrar por nombre de tarea
                .filter(t -> t.getNombreTarea().toLowerCase().contains(textoBusqueda))
                // 3. Filtrar por prioridad
                .filter(t -> finalNombrePrioridadFiltro.isEmpty() || t.getNombrePrioridad().equalsIgnoreCase(finalNombrePrioridadFiltro))
                .collect(Collectors.toList());

        if (vista.getBtnVistaLista().isSelected()) {
            mostrarTareasEnLista(tareasFiltradas);
        } else {
            mostrarTareasEnKanban(tareasFiltradas);
        }
    }

    private void mostrarTareasEnLista(List<Tarea> tareas) {
        CardLayout cl = (CardLayout) vista.getPanelContenedorPrincipal().getLayout();
        cl.show(vista.getPanelContenedorPrincipal(), "Lista");
        JPanel panelContenido = vista.getPanelContenidoLista();
        panelContenido.removeAll();
        for (Tarea tarea : tareas) {
            panelContenido.add(crearTarjeta(tarea));
            panelContenido.add(Box.createVerticalStrut(10));
        }
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarTareasEnKanban(List<Tarea> tareas) {
        CardLayout cl = (CardLayout) vista.getPanelContenedorPrincipal().getLayout();
        cl.show(vista.getPanelContenedorPrincipal(), "Kanban");
        Map<String, JPanel> columnas = vista.getColumnasKanban();
        columnas.values().forEach(JPanel::removeAll);
        for (Tarea tarea : tareas) {
            String estado = tarea.getNombreEstado().toUpperCase();
            if (columnas.containsKey(estado)) {
                columnas.get(estado).add(crearTarjeta(tarea));
                columnas.get(estado).add(Box.createVerticalStrut(10));
            }
        }
        columnas.values().forEach(c -> {
            c.revalidate();
            c.repaint();
        });
    }

    private TarjetaTareaPanel crearTarjeta(Tarea tarea) {
        TarjetaTareaPanel tarjeta = new TarjetaTareaPanel(tarea);
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                TareaDetalleVista detalleVista = new TareaDetalleVista();
                TareaDetalleControlador detalleControlador = new TareaDetalleControlador(detalleVista, vista, tarea.getIdTarea());
                detalleControlador.iniciar();
            }
        });
        return tarjeta;
    }
}