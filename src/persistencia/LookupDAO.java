// Archivo: src/persistencia/LookupDAO.java
package persistencia;

import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LookupDAO<T> {
    private static final Logger logger = Logger.getLogger(LookupDAO.class.getName());

    public List<T> listarTodos(String nombreTabla, RowMapper<T> mapper) {
        List<T> resultados = new ArrayList<>();
        String sql = "SELECT * FROM " + nombreTabla + " WHERE Estado = 'Activo'";

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                resultados.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error al listar la tabla " + nombreTabla + ": " + e.getMessage());
        }
        return resultados;
    }

    // --- NUEVO MÉTODO AÑADIDO ---
    public T obtenerPorCampoUnico(String nombreTabla, String columnaBusqueda, Object valorBusqueda, RowMapper<T> mapper) {
        // La sintaxis '?' previene inyección SQL
        String sql = "SELECT * FROM " + nombreTabla + " WHERE " + columnaBusqueda + " = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, valorBusqueda);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al obtener de " + nombreTabla + " por " + columnaBusqueda + ": " + e.getMessage());
        }
        return null;
    }
}