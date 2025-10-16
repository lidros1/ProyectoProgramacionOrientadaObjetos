package vista;

import modelo.Proyecto;
import javax.swing.*;
import java.awt.*;

public class TarjetaProyectoPanel extends JPanel {
    private Proyecto proyecto;

    public TarjetaProyectoPanel(Proyecto proyecto) {
        this.proyecto = proyecto;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel("<html><b>" + proyecto.getNombreProyecto() + "</b></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblPrioridad = new JLabel("Prioridad: " + proyecto.getNombrePrioridad());
        lblPrioridad.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblPrioridad);

        add(infoPanel, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

        JProgressBar progressBar = new JProgressBar(0, 100);
        int progressValue = proyecto.getPorcentajeAvance().intValue();
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setString(progressValue + "%");

        progressPanel.add(progressBar);
        add(progressPanel, BorderLayout.SOUTH);
    }

    public Proyecto getProyecto() {
        return proyecto;
    }
}