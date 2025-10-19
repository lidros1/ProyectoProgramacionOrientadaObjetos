// Archivo: src/vista/ReportesMenuVista.java
package vista;

import javax.swing.*;
import java.awt.*;

public class ReportesMenuVista extends JFrame {
    private JButton btnReportesProyectos;
    private JButton btnReportesTareas;
    private JButton btnReportesUsuarios;
    private JButton btnVolver;

    public ReportesMenuVista() {
        setTitle("Menú de Reportes");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        btnReportesProyectos = new JButton("Reportes de Proyectos");
        btnReportesTareas = new JButton("Reportes de Tareas");
        btnReportesUsuarios = new JButton("Reportes de Usuarios");
        btnVolver = new JButton("Volver al Menú Principal");

        gbc.gridy = 0; add(btnReportesProyectos, gbc);
        gbc.gridy = 1; add(btnReportesTareas, gbc);
        gbc.gridy = 2; add(btnReportesUsuarios, gbc);
        gbc.gridy = 3; add(new JSeparator(), gbc);
        gbc.gridy = 4; add(btnVolver, gbc);
    }

    // Getters
    public JButton getBtnReportesProyectos() { return btnReportesProyectos; }
    public JButton getBtnReportesTareas() { return btnReportesTareas; }
    public JButton getBtnReportesUsuarios() { return btnReportesUsuarios; }
    public JButton getBtnVolver() { return btnVolver; }
}