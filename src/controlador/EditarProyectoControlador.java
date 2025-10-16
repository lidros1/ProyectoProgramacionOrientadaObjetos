package controlador;

import modelo.Estado;
import modelo.Prioridad;
import modelo.Proyecto;
import persistencia.*;
import vista.DialogoEditarProyecto;
import vista.EditarProyectoVista;
import vista.TarjetaProyectoEditablePanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditarProyectoControlador implements ActionListener {
    private final EditarProyectoVista vista;
    private final JFrame vistaAnterior;
    private final ProyectoDAO proyectoDAO;
    private final PrioridadDAO prioridadDAO;
    private final EstadoDAO estadoDAO;
    private List<Proyecto> todosLosProyectos;

    public EditarProyectoControlador(EditarProyectoVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;
        this.proyectoDAO = new ProyectoDAO();
        this.prioridadDAO = new PrioridadDAO();
        this.estadoDAO = new EstadoDAO();

        this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos();

        this.vista.getBtnVolver().addActionListener(this);
        this.vista.getComboFiltroPrioridad().addActionListener(this);
        this.vista.getBtnVistaLista().addActionListener(this);
        this.vista.getBtnVistaKanban().addActionListener(this);
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
            TarjetaProyectoEditablePanel tarjeta = crearTarjeta(proyecto);
            panelContenido.add(tarjeta);
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
                TarjetaProyectoEditablePanel tarjeta = crearTarjeta(proyecto);
                columnas.get(estado).add(tarjeta);
                columnas.get(estado).add(Box.createVerticalStrut(10));
            }
        }

        columnas.values().forEach(c -> {
            c.revalidate();
            c.repaint();
        });
    }

    private TarjetaProyectoEditablePanel crearTarjeta(Proyecto proyecto) {
        TarjetaProyectoEditablePanel tarjeta = new TarjetaProyectoEditablePanel(proyecto);
        tarjeta.getBtnEditar().addActionListener(e -> abrirDialogoEdicion(proyecto));
        return tarjeta;
    }

    private void abrirDialogoEdicion(Proyecto proyecto) {
        List<Prioridad> prioridades = prioridadDAO.listarTodos();
        List<Estado> estados = estadoDAO.listarTodos();

        DialogoEditarProyecto dialogo = new DialogoEditarProyecto(vista, proyecto, prioridades, estados);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            Proyecto proyectoActualizado = dialogo.getProyectoActualizado();

            boolean exitoProyecto = proyectoDAO.actualizar(proyectoActualizado);

            if (exitoProyecto) {
                JOptionPane.showMessageDialog(vista, "Proyecto actualizado correctamente.");
                this.todosLosProyectos = proyectoDAO.listarTodosLosProyectos(); // Recargar datos
                filtrarYMostrarProyectos(); // Refrescar la vista
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el proyecto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}