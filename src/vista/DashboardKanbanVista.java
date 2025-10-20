// Archivo: src/vista/DashboardKanbanVista.java
package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DashboardKanbanVista extends JFrame {
    private final JTabbedPane panelConPestanas;
    private final JButton botonVerTodasMisTareas;
    private final JComboBox<String> comboProyectosTareas;
    private final Map<String, JPanel> panelesColumnasProyectos;
    private final Map<String, JPanel> panelesColumnasTareas;
    private static final Logger logger = Logger.getLogger(DashboardKanbanVista.class.getName());

    public static final String[] COLUMNAS_KANBAN_TAREAS = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "HECHO"};
    public static final String[] COLUMNAS_KANBAN_PROYECTOS = {"POR HACER", "EN PROGRESO", "EN REVISIÓN", "BLOQUEADO", "HECHO"};

    public DashboardKanbanVista() {
        setTitle("Panel Principal de Proyectos y Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);

        panelConPestanas = new JTabbedPane();
        panelConPestanas.setFont(ConstantesUI.FUENTE_NORMAL);

        // --- Pestaña de Proyectos ---
        JPanel panelContenedorProyectos = new JPanel(new BorderLayout());
        panelContenedorProyectos.setOpaque(false);
        JPanel panelProyectos = new JPanel(new GridLayout(1, COLUMNAS_KANBAN_PROYECTOS.length, 10, 10));
        panelProyectos.setOpaque(false);
        panelProyectos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelesColumnasProyectos = new HashMap<>();
        for (String estado : COLUMNAS_KANBAN_PROYECTOS) {
            JPanel columna = crearColumnaKanban(estado);
            panelesColumnasProyectos.put(estado, columna);
            JScrollPane scroll = new JScrollPane(columna);
            scroll.setBorder(null);
            scroll.getViewport().setOpaque(false);
            panelProyectos.add(scroll);
        }
        panelContenedorProyectos.add(panelProyectos, BorderLayout.CENTER);
        panelConPestanas.addTab("Mis Proyectos", panelContenedorProyectos);

        // --- Pestaña de Tareas ---
        JPanel panelContenedorTareas = new JPanel(new BorderLayout());
        panelContenedorTareas.setOpaque(false);

        JPanel panelTareas = new JPanel(new GridLayout(1, COLUMNAS_KANBAN_TAREAS.length, 10, 10));
        panelTareas.setOpaque(false);
        panelTareas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelesColumnasTareas = new HashMap<>();
        for (String estado : COLUMNAS_KANBAN_TAREAS) {
            JPanel columna = crearColumnaKanban(estado);
            panelesColumnasTareas.put(estado, columna);
            JScrollPane scroll = new JScrollPane(columna);
            scroll.setBorder(null);
            scroll.getViewport().setOpaque(false);
            panelTareas.add(scroll);
        }

        // Panel superior de la pestaña de tareas
        JPanel panelSuperiorTareas = new JPanel(new BorderLayout());
        panelSuperiorTareas.setOpaque(false);
        panelSuperiorTareas.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        comboProyectosTareas = new JComboBox<>();
        comboProyectosTareas.setFont(ConstantesUI.FUENTE_NORMAL);
        comboProyectosTareas.addItem("Todas");
        JPanel panelFiltroTareas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltroTareas.setOpaque(false);
        panelFiltroTareas.add(new JLabel("Filtrar por proyecto:"));
        panelFiltroTareas.add(comboProyectosTareas);

        botonVerTodasMisTareas = new JButton("Ver Todas Mis Tareas");
        TemaPersonalizado.aplicarEstiloBotonPrincipal(botonVerTodasMisTareas);

        panelSuperiorTareas.add(panelFiltroTareas, BorderLayout.WEST);
        panelSuperiorTareas.add(botonVerTodasMisTareas, BorderLayout.EAST);

        panelContenedorTareas.add(panelSuperiorTareas, BorderLayout.NORTH);
        panelContenedorTareas.add(panelTareas, BorderLayout.CENTER);

        panelConPestanas.addTab("Tareas de Proyecto Seleccionado", panelContenedorTareas);
        add(panelConPestanas);
    }

    private JPanel crearColumnaKanban(String titulo) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBorder(BorderFactory.createTitledBorder(titulo));
        TemaPersonalizado.aplicarEstiloColumnaKanban(columna);
        return columna;
    }

    public Map<String, JPanel> getPanelesColumnasProyectos() { return panelesColumnasProyectos; }
    public Map<String, JPanel> getPanelesColumnasTareas() { return panelesColumnasTareas; }

    public void configurarDropEnColumnasTareas(Consumer<TarjetaTareaPanel> onDrop) {
        for (JPanel columna : panelesColumnasTareas.values()) {
            columna.setTransferHandler(createTransferHandler(TarjetaTareaPanel.class, onDrop));
        }
    }

    public void configurarDropEnColumnasProyectos(Consumer<TarjetaProyectoPanel> onDrop) {
        for (JPanel columna : panelesColumnasProyectos.values()) {
            columna.setTransferHandler(createTransferHandler(TarjetaProyectoPanel.class, onDrop));
        }
    }

    private <T extends JPanel> TransferHandler createTransferHandler(Class<T> tarjetaClass, Consumer<T> onDrop) {
        return new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDrop() && support.isDataFlavorSupported(new DataFlavor(tarjetaClass, tarjetaClass.getSimpleName()));
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                try {
                    T tarjeta = (T) support.getTransferable().getTransferData(new DataFlavor(tarjetaClass, tarjetaClass.getSimpleName()));
                    JPanel panelDestino = (JPanel) support.getComponent();

                    // Mover la tarjeta visualmente
                    Container panelOrigen = tarjeta.getParent();
                    if (panelOrigen != null) {
                        panelOrigen.remove(tarjeta);
                        panelOrigen.revalidate();
                        panelOrigen.repaint();
                    }
                    panelDestino.add(tarjeta);
                    panelDestino.revalidate();
                    panelDestino.repaint();

                    // Notificar al controlador para actualizar la base de datos
                    if (onDrop != null) {
                        onDrop.accept(tarjeta);
                    }
                    return true;
                } catch (Exception ex) {
                    logger.warning("Error en importData: " + ex.getMessage());
                    return false;
                }
            }
        };
    }

    public JTabbedPane getPanelConPestanas() { return panelConPestanas; }
    public JButton getBotonVerTodasMisTareas() { return botonVerTodasMisTareas; }
    public JComboBox<String> getComboProyectosTareas() { return comboProyectosTareas; }
}