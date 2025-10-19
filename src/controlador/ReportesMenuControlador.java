// Archivo: src/controlador/ReportesMenuControlador.java
package controlador;

import vista.ReportesMenuVista;
import vista.ReportesVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportesMenuControlador implements ActionListener {
    private final ReportesMenuVista vista;
    private final JFrame vistaAnterior;

    public ReportesMenuControlador(ReportesMenuVista vista, JFrame vistaAnterior) {
        this.vista = vista;
        this.vistaAnterior = vistaAnterior;

        this.vista.getBtnReportesProyectos().addActionListener(this);
        this.vista.getBtnReportesTareas().addActionListener(this);
        this.vista.getBtnReportesUsuarios().addActionListener(this);
        this.vista.getBtnVolver().addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            vista.dispose();
            vistaAnterior.setVisible(true);
            return;
        }

        vista.setVisible(false);
        ReportesVista reportesVista = new ReportesVista();
        ReportesControlador reportesControlador = null;

        if (e.getSource() == vista.getBtnReportesProyectos()) {
            reportesControlador = new ReportesControlador(reportesVista, vista, "PROYECTOS");
        } else if (e.getSource() == vista.getBtnReportesTareas()) {
            reportesControlador = new ReportesControlador(reportesVista, vista, "TAREAS");
        } else if (e.getSource() == vista.getBtnReportesUsuarios()) {
            reportesControlador = new ReportesControlador(reportesVista, vista, "USUARIOS");
        }

        if (reportesControlador != null) {
            reportesControlador.iniciar();
        }
    }
}