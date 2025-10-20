// Archivo: src/vista/ReportesVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class ReportesVista extends JFrame {
    private JPanel panelCentral;
    private JButton btnVolver;

    public ReportesVista() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        TemaPersonalizado.configurarVentana(this);
        setLayout(new BorderLayout(10, 10));

        // El panel central será un contenedor para los reportes
        panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // --- SOLUCIÓN: Usamos un JScrollPane para permitir el desplazamiento vertical ---
        JScrollPane scrollPane = new JScrollPane(panelCentral);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        // Aumentar la velocidad del scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para el botón 'Volver'
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelSur.setOpaque(false);
        btnVolver = new JButton("Volver");
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);
    }

    public JPanel getPanelCentral() {
        return panelCentral;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
}