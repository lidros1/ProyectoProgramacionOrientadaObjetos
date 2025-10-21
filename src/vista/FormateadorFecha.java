// Archivo: src/vista/FormateadorFecha.java
package vista;

import javax.swing.JFormattedTextField;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Formateador personalizado para los componentes JDatePicker.
 * Asegura que la fecha se muestre consistentemente en formato yyyy-MM-dd.
 */
public class FormateadorFecha extends JFormattedTextField.AbstractFormatter {
    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            if (value instanceof Date) {
                return dateFormatter.format((Date) value);
            } else if (value instanceof Calendar) {
                return dateFormatter.format(((Calendar) value).getTime());
            }
        }
        return "";
    }
}