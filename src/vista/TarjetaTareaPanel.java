// Archivo: src/vista/TarjetaTareaPanel.java
package vista;

import modelo.Tarea;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class TarjetaTareaPanel extends JPanel {
    private Tarea tarea;

    public TarjetaTareaPanel(Tarea tarea) {
        this.tarea = tarea;
        setLayout(new BorderLayout(5, 5));

        // Borde y tamaño
        setBorder(ConstantesUI.BORDE_COMPUESTO);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        setMinimumSize(new Dimension(200, 95));
        setPreferredSize(new Dimension(250, 95));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText("Clic para ver detalles de la tarea");

        // Asignar color de fondo según la prioridad
        switch (tarea.getNombrePrioridad().toUpperCase()) {
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

        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblNombre = new JLabel("<html><body style='width: 180px'>" + tarea.getNombreTarea() + "</body></html>");
        lblNombre.setFont(ConstantesUI.FUENTE_SUBTITULO);
        lblNombre.setForeground(ConstantesUI.COLOR_TEXTO_PRINCIPAL);

        JLabel lblPrioridad = new JLabel("Prioridad: " + tarea.getNombrePrioridad());
        lblPrioridad.setFont(ConstantesUI.FUENTE_PEQUENA);
        lblPrioridad.setForeground(ConstantesUI.COLOR_TEXTO_SECUNDARIO);

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblPrioridad);

        add(infoPanel, BorderLayout.CENTER);

        // Barra de progreso
        JProgressBar progressBar = new JProgressBar(0, 100);
        BigDecimal porcentajeAvance = tarea.getPorcentajeAvance();
        int progressValue = (porcentajeAvance != null) ? porcentajeAvance.intValue() : 0;
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setString(progressValue + "%");
        progressBar.setFont(ConstantesUI.FUENTE_PEQUENA);

        add(progressBar, BorderLayout.SOUTH);
    }

    public Tarea getTarea() {
        return tarea;
    }
}