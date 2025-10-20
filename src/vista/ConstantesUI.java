// Archivo: src/vista/ConstantesUI.java
package vista;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;

/**
 * Clase de utilidad para centralizar las constantes de la interfaz de usuario.
 * Define colores, fuentes, bordes y otros estilos para mantener una
 * apariencia consistente en toda la aplicación.
 */
public final class ConstantesUI {

    // --- Paleta de Colores Principal ---
    public static final Color COLOR_FONDO_PRINCIPAL = new Color(245, 245, 245); // Gris muy claro
    public static final Color COLOR_FONDO_SECUNDARIO = new Color(255, 255, 255); // Blanco
    public static final Color COLOR_PRIMARIO = new Color(70, 130, 180);         // Azul acero
    public static final Color COLOR_SECUNDARIO = new Color(119, 136, 153);      // Gris pizarra claro
    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(40, 40, 40);          // Casi negro
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(100, 100, 100);     // Gris oscuro
    public static final Color COLOR_BORDE = new Color(220, 220, 220);             // Gris claro

    // --- Colores para Prioridades ---
    public static final Color COLOR_PRIORIDAD_ALTA = new Color(255, 224, 224);       // Rojo pálido
    public static final Color COLOR_PRIORIDAD_MEDIA = new Color(255, 248, 225);      // Amarillo pálido
    public static final Color COLOR_PRIORIDAD_BAJA = new Color(240, 248, 255);       // Azul pálido

    // --- Fuentes ---
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FUENTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    // --- Bordes ---
    public static final Border BORDE_SIMPLE = BorderFactory.createLineBorder(COLOR_BORDE, 1);
    public static final Border BORDE_COMPUESTO = BorderFactory.createCompoundBorder(
            BORDE_SIMPLE,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    public static final Border BORDE_TITULO(String titulo) {
        return BorderFactory.createTitledBorder(
                BORDE_SIMPLE,
                titulo,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                FUENTE_SUBTITULO,
                COLOR_PRIMARIO
        );
    }

    // --- Prevenir Instanciación ---
    private ConstantesUI() {}
}