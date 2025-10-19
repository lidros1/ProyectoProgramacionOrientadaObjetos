// Archivo: src/vista/TarjetaProyectoPanel.java
package vista;

import modelo.Proyecto;
import javax.swing.*;
import java.awt.*;

public class TarjetaProyectoPanel extends JPanel {
    private Proyecto proyecto;
    private JButton btnEditar; // Puede ser nulo

    public TarjetaProyectoPanel(Proyecto proyecto, boolean esEditable) {
        this.proyecto = proyecto;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Panel de información (Nombre y Prioridad)
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

        // Panel inferior
        JPanel panelSur = new JPanel(new BorderLayout(5, 5));

        // Barra de progreso
        JProgressBar progressBar = new JProgressBar(0, 100);
        int progressValue = (proyecto.getPorcentajeAvance() != null) ? proyecto.getPorcentajeAvance().intValue() : 0;
        progressBar.setValue(progressValue);
        progressBar.setStringPainted(true);
        progressBar.setString(progressValue + "%");
        panelSur.add(progressBar, BorderLayout.CENTER);

        // Botón de editar (condicional)
        if (esEditable) {
            btnEditar = new JButton("Editar");
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