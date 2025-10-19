package controlador;

import modelo.Estado;
import modelo.Proyecto;
import modelo.Tarea;
import persistencia.JerarquiaUsuarioDAO;
import persistencia.LookupDAO;
import persistencia.ProyectoDAO;
import persistencia.TareaDAO;
import util.SesionUsuario;
import vista.DashboardKanbanVista;
import vista.TarjetaProyectoPanel;
import vista.TarjetaTareaPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

public class DashboardControlador {
    private static final Logger logger = Logger.getLogger(DashboardControlador.class.getName());
    private final DashboardKanbanVista vista;
    private final ProyectoDAO proyectoDAO;
    private final TareaDAO tareaDAO;
    private final JerarquiaUsuarioDAO jerarquiaDAO;
    private final LookupDAO<Estado> estadoLookupDAO; // DAO Genérico para Estados

    public DashboardControlador(DashboardKanbanVista vista) {
        this.vista = vista;
        this.proyectoDAO = new ProyectoDAO();
        this.tareaDAO = new TareaDAO();
        this.jerarquiaDAO = new JerarquiaUsuarioDAO();
        this.estadoLookupDAO = new LookupDAO<>(); // Inicializar el DAO genérico

        this.vista.getPanelConPestanas().addChangeListener(e -> {
            if (vista.getPanelConPestanas().getSelectedIndex() == 0) {
                cargarProyectos();
            }
        });
    }

    public void iniciar() {
        // --- LÓGICA DE DROP MODIFICADA PARA USAR LOOKUPDAO ---
        vista.configurarDropEnColumnasProyectos(tarjeta -> {
            Proyecto proyecto = tarjeta.getProyecto();
            Container parent = tarjeta.getParent();
            if (parent instanceof JPanel) {
                String nuevoEstadoNombre = ((javax.swing.border.TitledBorder) ((JPanel) parent).getBorder()).getTitle();

                Estado estadoObj = estadoLookupDAO.obtenerPorCampoUnico("estados", "NombreEstado", nuevoEstadoNombre, rs -> {
                    Estado e = new Estado();
                    e.setIdEstado(rs.getInt("IDEstado"));
                    return e;
                });

                if (estadoObj != null) {
                    proyectoDAO.actualizarEstado(proyecto.getIdProyecto(), estadoObj.getIdEstado());
                    cargarProyectos();
                }
            }
        });
        vista.configurarDropEnColumnasTareas(tarjeta -> {
            Tarea tarea = tarjeta.getTarea();
            Container parent = tarjeta.getParent();
            if (parent instanceof JPanel) {
                String nuevoEstadoNombre = ((javax.swing.border.TitledBorder) ((JPanel) parent).getBorder()).getTitle();

                Estado estadoObj = estadoLookupDAO.obtenerPorCampoUnico("estados", "NombreEstado", nuevoEstadoNombre, rs -> {
                    Estado e = new Estado();
                    e.setIdEstado(rs.getInt("IDEstado"));
                    return e;
                });

                if (estadoObj != null) {
                    tareaDAO.actualizarEstado(tarea.getIdTarea(), estadoObj.getIdEstado());
                    cargarProyectos();
                }
            }
        });
        // --------------------------------------------------------

        cargarProyectos();
        vista.setVisible(true);
    }

    private void cargarProyectos() {
        limpiarPanelesProyectos();
        int idUsuario = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        List<Proyecto> proyectos = proyectoDAO.listarProyectosPorUsuario(idUsuario);

        for (Proyecto proyecto : proyectos) {
            String estadoColumna = mapearEstadoAColumna(proyecto.getNombreEstado());
            JPanel columna = vista.getPanelesColumnasProyectos().get(estadoColumna);

            if (columna != null) {
                TarjetaProyectoPanel tarjeta = new TarjetaProyectoPanel(proyecto, false);
                tarjeta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            cargarTareasDelProyecto(proyecto);
                            vista.getPanelConPestanas().setSelectedIndex(1);
                        }
                    }
                });
                columna.add(tarjeta);
                columna.add(Box.createVerticalStrut(10));
            }
        }
        vista.revalidate();
        vista.repaint();
    }

    private void cargarTareasDelProyecto(Proyecto proyecto) {
        limpiarPanelesTareas();
        int idUsuario = SesionUsuario.getUsuarioLogueado().getIdUsuario();
        List<Tarea> tareas;
        if (proyecto.getIdProyecto() == 0) {
            tareas = tareaDAO.listarTodasTareasPorUsuario(idUsuario);
        } else {
            tareas = tareaDAO.listarTareasPorProyectoYUsuario(proyecto.getIdProyecto(), idUsuario);
        }
        for (Tarea tarea : tareas) {
            String estadoColumna = mapearEstadoAColumna(tarea.getNombreEstado());
            JPanel columna = vista.getPanelesColumnasTareas().get(estadoColumna);
            if (columna != null) {
                TarjetaTareaPanel tarjeta = new TarjetaTareaPanel(tarea);
                columna.add(tarjeta);
                columna.add(Box.createVerticalStrut(10));
            }
        }
        vista.revalidate();
        vista.repaint();
    }

    private void limpiarPanelesProyectos() {
        for (JPanel panel : vista.getPanelesColumnasProyectos().values()) {
            panel.removeAll();
        }
    }

    private void limpiarPanelesTareas() {
        for (JPanel panel : vista.getPanelesColumnasTareas().values()) {
            panel.removeAll();
        }
    }

    private String mapearEstadoAColumna(String nombreEstadoDB) {
        if (nombreEstadoDB == null) return "Bloqueado";
        return switch (nombreEstadoDB.toUpperCase()) {
            case "POR HACER" -> "Por hacer";
            case "EN PROGRESO" -> "En progreso";
            case "EN REVISIÓN" -> "En revisión";
            case "HECHO" -> "Hecho";
            default -> "Bloqueado";
        };
    }
}