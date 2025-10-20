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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Corrección de tamaño y estilo de fondo
        TemaPersonalizado.configurarVentana(this);

        // Panel central para los botones
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.ipadx = 100;

        btnReportesProyectos = new JButton("Reportes de Proyectos");
        btnReportesTareas = new JButton("Reportes de Tareas");
        btnReportesUsuarios = new JButton("Reportes de Usuarios");
        btnVolver = new JButton("Volver al Menú Principal");

        // Aplicar estilos
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnReportesProyectos);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnReportesTareas);
        TemaPersonalizado.aplicarEstiloBotonPrincipal(btnReportesUsuarios);
        TemaPersonalizado.aplicarEstiloBotonSecundario(btnVolver);

        gbc.gridy = 0; panelCentral.add(btnReportesProyectos, gbc);
        gbc.gridy = 1; panelCentral.add(btnReportesTareas, gbc);
        gbc.gridy = 2; panelCentral.add(btnReportesUsuarios, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.gridy = 3; panelCentral.add(btnVolver, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Getters
    public JButton getBtnReportesProyectos() { return btnReportesProyectos; }
    public JButton getBtnReportesTareas() { return btnReportesTareas; }
    public JButton getBtnReportesUsuarios() { return btnReportesUsuarios; }
    public JButton getBtnVolver() { return btnVolver; }
}