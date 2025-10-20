// Archivo: src/vista/TemaPersonalizado.java
package vista;

import javax.swing.*;
import java.awt.Color;
import java.awt.Cursor;

/**
 * Clase de utilidad para aplicar estilos consistentes a los componentes Swing.
 * Utiliza las constantes definidas en ConstantesUI para configurar la apariencia
 * de botones, paneles y otros elementos.
 */
public final class TemaPersonalizado {

    /**
     * Aplica el estilo estándar a un botón principal (acciones primarias).
     * @param button El JButton al que se le aplicará el estilo.
     */
    public static void aplicarEstiloBotonPrincipal(JButton button) {
        button.setFont(ConstantesUI.FUENTE_BOTON);
        button.setBackground(ConstantesUI.COLOR_PRIMARIO);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ConstantesUI.COLOR_SECUNDARIO, 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
    }

    /**
     * Aplica el estilo a un botón secundario (acciones como 'Volver' o 'Cancelar').
     * @param button El JButton al que se le aplicará el estilo.
     */
    public static void aplicarEstiloBotonSecundario(JButton button) {
        button.setFont(ConstantesUI.FUENTE_BOTON);
        button.setBackground(ConstantesUI.COLOR_FONDO_SECUNDARIO);
        button.setForeground(ConstantesUI.COLOR_TEXTO_PRINCIPAL);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ConstantesUI.COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
    }

    /**
     * Aplica el estilo a un JToggleButton.
     * @param button El JToggleButton al que se le aplicará el estilo.
     */
    public static void aplicarEstiloBotonAlternar(JToggleButton button) {
        button.setFont(ConstantesUI.FUENTE_NORMAL);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(ConstantesUI.COLOR_BORDE, 1));
    }

    /**
     * Aplica el estilo a un panel de columna Kanban.
     * @param panel El JPanel que representa la columna.
     */
    public static void aplicarEstiloColumnaKanban(JPanel panel) {
        panel.setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);
        panel.setBorder(ConstantesUI.BORDE_TITULO(((javax.swing.border.TitledBorder) panel.getBorder()).getTitle()));
    }

    /**
     * Configura una ventana (JFrame) para que se abra maximizada y con un fondo estándar.
     * @param frame El JFrame a configurar.
     */
    public static void configurarVentana(JFrame frame) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana
        frame.getContentPane().setBackground(ConstantesUI.COLOR_FONDO_PRINCIPAL);
    }

    private TemaPersonalizado() {}
}