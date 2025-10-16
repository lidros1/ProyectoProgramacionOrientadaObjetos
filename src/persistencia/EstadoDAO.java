package persistencia;

import modelo.Estado;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {
    /**
     * Lista todos los estados activos de la base de datos.
     * @return Una lista de objetos Estado.
     */
    public List<Estado> listarTodos() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT IDEstado, NombreEstado, DescripcionEstado FROM estados WHERE Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Estado estado = new Estado();
                estado.setIdEstado(rs.getInt("IDEstado"));
                estado.setNombreEstado(rs.getString("NombreEstado"));
                estado.setDescripcionEstado(rs.getString("DescripcionEstado"));
                estados.add(estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estados;
    }

    /**
     * Obtiene el ID de un estado a partir de su nombre.
     * @param nombreEstado El nombre del estado (ej. "En progreso").
     * @return El ID del estado, o -1 si no se encuentra.
     */
    public int obtenerIdPorNombre(String nombreEstado) {
        String sql = "SELECT IDEstado FROM estados WHERE NombreEstado = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEstado);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("IDEstado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra o hay un error
    }
}