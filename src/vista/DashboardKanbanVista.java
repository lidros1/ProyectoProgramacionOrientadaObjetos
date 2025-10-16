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
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panelConPestanas = new JTabbedPane();

        JPanel panelProyectos = new JPanel(new GridLayout(1, COLUMNAS_KANBAN_PROYECTOS.length, 10, 10));
        panelProyectos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelesColumnasProyectos = new HashMap<>();
        for (String estado : COLUMNAS_KANBAN_PROYECTOS) {
            JPanel columna = crearColumnaKanban(estado);
            panelesColumnasProyectos.put(estado, columna);
            panelProyectos.add(columna);
        }
        panelConPestanas.addTab("Mis Proyectos", new JScrollPane(panelProyectos));

        JPanel panelTareas = new JPanel(new GridLayout(1, COLUMNAS_KANBAN_TAREAS.length, 10, 10));
        panelTareas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelesColumnasTareas = new HashMap<>();
        for (String estado : COLUMNAS_KANBAN_TAREAS) {
            JPanel columna = crearColumnaKanban(estado);
            // --- CORRECCIÓN AQUÍ: Se usa el nombre correcto de la variable ---
            panelesColumnasTareas.put(estado, columna);
            panelTareas.add(columna);
        }

        JPanel panelTareasConBoton = new JPanel(new BorderLayout());

        comboProyectosTareas = new JComboBox<>();
        comboProyectosTareas.addItem("Todas");
        JPanel panelFiltroTareas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltroTareas.add(new JLabel("Filtrar por proyecto:"));
        panelFiltroTareas.add(comboProyectosTareas);

        botonVerTodasMisTareas = new JButton("Ver Todas Mis Tareas (en todos los proyectos)");
        JPanel panelBotonTareas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonTareas.add(botonVerTodasMisTareas);

        JPanel panelSuperiorTareas = new JPanel(new BorderLayout());
        panelSuperiorTareas.add(panelFiltroTareas, BorderLayout.WEST);
        panelSuperiorTareas.add(panelBotonTareas, BorderLayout.EAST);

        panelTareasConBoton.add(panelSuperiorTareas, BorderLayout.NORTH);
        panelTareasConBoton.add(new JScrollPane(panelTareas), BorderLayout.CENTER);

        panelConPestanas.addTab("Tareas de Proyecto Seleccionado", panelTareasConBoton);
        add(panelConPestanas);
    }

    private JPanel crearColumnaKanban(String titulo) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setBorder(BorderFactory.createTitledBorder(titulo));
        columna.setBackground(new Color(240, 240, 240));
        return columna;
    }

    public Map<String, JPanel> getPanelesColumnasProyectos() { return panelesColumnasProyectos; }
    public Map<String, JPanel> getPanelesColumnasTareas() { return panelesColumnasTareas; }

    public void configurarDropEnColumnasTareas(Consumer<TarjetaTareaPanel> onDrop) {
        for (JPanel columna : panelesColumnasTareas.values()) {
            columna.setTransferHandler(new TransferHandler() {
                @Override
                public boolean canImport(TransferSupport support) {
                    return support.getComponent() instanceof JPanel && support.isDrop();
                }
                @Override
                public boolean importData(TransferSupport support) {
                    try {
                        JPanel panelDestino = (JPanel) support.getComponent();
                        DataFlavor tarjetaFlavor = new DataFlavor(TarjetaTareaPanel.class, "TarjetaTareaPanel");
                        TarjetaTareaPanel tarjeta = (TarjetaTareaPanel) support.getTransferable().getTransferData(tarjetaFlavor);
                        Container panelOrigen = tarjeta.getParent();
                        if (panelOrigen != null) panelOrigen.remove(tarjeta);
                        panelDestino.add(tarjeta);
                        panelDestino.revalidate();
                        panelDestino.repaint();
                        if (onDrop != null) onDrop.accept(tarjeta);
                        return true;
                    } catch (Exception ex) {
                        logger.warning("Error en importData (Tareas): " + ex.getMessage());
                        return false;
                    }
                }
            });
        }
    }

    public void configurarDropEnColumnasProyectos(Consumer<TarjetaProyectoPanel> onDrop) {
        for (JPanel columna : panelesColumnasProyectos.values()) {
            columna.setTransferHandler(new TransferHandler() {
                @Override
                public boolean canImport(TransferSupport support) {
                    return support.getComponent() instanceof JPanel && support.isDrop();
                }
                @Override
                public boolean importData(TransferSupport support) {
                    try {
                        JPanel panelDestino = (JPanel) support.getComponent();
                        DataFlavor tarjetaFlavor = new DataFlavor(TarjetaProyectoPanel.class, "TarjetaProyectoPanel");
                        TarjetaProyectoPanel tarjeta = (TarjetaProyectoPanel) support.getTransferable().getTransferData(tarjetaFlavor);
                        Container panelOrigen = tarjeta.getParent();
                        if (panelOrigen != null) panelOrigen.remove(tarjeta);
                        panelDestino.add(tarjeta);
                        panelDestino.revalidate();
                        panelDestino.repaint();
                        if (onDrop != null) onDrop.accept(tarjeta);
                        return true;
                    } catch (Exception ex) {
                        logger.warning("Error en importData (Proyectos): " + ex.getMessage());
                        return false;
                    }
                }
            });
        }
    }

    public JTabbedPane getPanelConPestanas() { return panelConPestanas; }
    public JButton getBotonVerTodasMisTareas() { return botonVerTodasMisTareas; }
    public JComboBox<String> getComboProyectosTareas() { return comboProyectosTareas; }
}