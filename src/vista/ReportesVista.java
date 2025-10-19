// Archivo: src/vista/ReportesVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class ReportesVista extends JFrame {
    private JPanel panelCentral;
    private JButton btnVolver;

    public ReportesVista() {
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        panelCentral = new JPanel(new BorderLayout());
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
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