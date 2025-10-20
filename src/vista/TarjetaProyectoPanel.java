// Archivo: src/vista/TarjetaProyectoPanel.java
package vista;

import modelo.Proyecto;
import javax.swing.*;
import java.awt.*;

public class TarjetaProyectoPanel extends JPanel {
    private Proyecto proyecto;
    private JButton btnEditar;

    public TarjetaProyectoPanel(Proyecto proyecto, boolean esEditable) {
        this.proyecto = proyecto;
        setLayout(new BorderLayout(5, 5));

        // Borde y tamaño
        setBorder(ConstantesUI.BORDE_COMPUESTO);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        setMinimumSize(new Dimension(200, 110));
        setPreferredSize(new Dimension(250, 110));

        // Asignar color de fondo según la prioridad
        switch (proyecto.getNombrePrioridad().toUpperCase()) {
            case "ALTA":
                setBackground(ConstantesUI.COLOR_PRIORIDAD_ALTA);
                break;
            case "MEDIA":
                setBackground(ConstantesUI.COLOR_PRIORIDAD_MEDIA);
                break;
            case "BAJA":
                setBackground(ConstantesUI.COLOR_PRIORIDAD_BAJA);
                break;
            default:
                setBackground(ConstantesUI.COLOR_FONDO_SECUNDARIO);
                break;
        }

        // Panel de información superior
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblNombre = new JLabel("<html><body style='width: 180px'>" + proyecto.getNombreProyecto() + "</body></html>");
        lblNombre.setFont(ConstantesUI.FUENTE_SUBTITULO);
        lblNombre.setForeground(ConstantesUI.COLOR_TEXTO_PRINCIPAL);

        JLabel lblPrioridad = new JLabel(proyecto.getNombrePrioridad());
        lblPrioridad.setFont(ConstantesUI.FUENTE_PEQUENA);
        lblPrioridad.setForeground(ConstantesUI.COLOR_TEXTO_SECUNDARIO);

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblPrioridad);

        add(infoPanel, BorderLayout.CENTER);

        // Panel inferior para progreso y botón
        JPanel panelSur = new JPanel(new BorderLayout(10, 0));
        panelSur.setOpaque(false);

        JProgressBar progressBar = new JProgressBar(0, 100);
        int progressValue = (proyecto.getPorcentajeAvance() != null) ? proyecto.getPorcentajeAvance().intValue() : 0;
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setString(progressValue + "%");
        progressBar.setFont(ConstantesUI.FUENTE_PEQUENA);
        panelSur.add(progressBar, BorderLayout.CENTER);

        if (esEditable) {
            btnEditar = new JButton("✎");
            btnEditar.setToolTipText("Editar Proyecto");
            // Estilo minimalista para el botón de editar
            btnEditar.setMargin(new Insets(2, 5, 2, 5));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panelSur.add(btnEditar, BorderLayout.EAST);
        }

        add(panelSur, BorderLayout.SOUTH);
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }
}