// Archivo: src/vista/RenderizadorComentario.java
package vista;

import modelo.Comentario;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Renderizador personalizado para mostrar objetos Comentario en una JList.
 * Formatea el comentario para mostrar el autor, la fecha y el contenido de forma clara.
 */
public class RenderizadorComentario extends DefaultListCellRenderer {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Usamos un JPanel para tener más control sobre el padding y el fondo
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (value instanceof Comentario c) {
            // Construimos el texto HTML para el label
            String text = String.format(
                    "<html><body style='width: %dpx;'><b>%s</b> <font color='gray'>(%s)</font><br>%s</body></html>",
                    list.getWidth() > 150 ? list.getWidth() - 50 : 300, // Ancho dinámico
                    c.getNombreUsuarioComentario(),
                    sdf.format(c.getFechaCreacion()),
                    c.getContenido().replaceAll("\n", "<br>") // Reemplaza saltos de línea
            );

            JLabel label = new JLabel(text);
            label.setFont(ConstantesUI.FUENTE_NORMAL);
            panel.add(label, BorderLayout.CENTER);
        }

        // Manejar los colores de selección
        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
        } else {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }

        return panel;
    }
}