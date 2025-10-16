package vista;

import modelo.Tarea;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal; // Importar BigDecimal

public class TarjetaTareaPanel extends JPanel {
    private Tarea tarea;

    public TarjetaTareaPanel(Tarea tarea) {
        this.tarea = tarea;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90)); // Altura fija para tareas
        setBackground(new Color(248, 248, 255)); // Un color más suave

        // Panel de información (Nombre y Prioridad)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false); // Para que el color de fondo del padre se vea

        JLabel lblNombre = new JLabel("<html><b>" + tarea.getNombreTarea() + "</b></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblPrioridad = new JLabel("Prioridad: " + tarea.getNombrePrioridad());
        lblPrioridad.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblPrioridad);

        add(infoPanel, BorderLayout.CENTER);

        // Panel de progreso (Barra y Porcentaje)
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setOpaque(false);

        JProgressBar progressBar = new JProgressBar(0, 100);
        BigDecimal porcentajeAvance = tarea.getPorcentajeAvance();
        int progressValue = (porcentajeAvance != null) ? porcentajeAvance.intValue() : 0;
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setString(progressValue + "%");

        progressPanel.add(progressBar);
        add(progressPanel, BorderLayout.SOUTH);
    }

    // Getter para que el controlador sepa a qué tarea se hizo clic
    public Tarea getTarea() {
        return tarea;
    }
}