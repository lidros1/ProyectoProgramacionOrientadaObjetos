package persistencia;

import modelo.Prioridad;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PrioridadDAO {
    public List<Prioridad> listarTodos() {
        List<Prioridad> prioridades = new ArrayList<>();
        String sql = "SELECT IDPrioridad, NombrePrioridad, DescripcionPrioridad FROM prioridades WHERE Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prioridad prioridad = new Prioridad();
                prioridad.setIdPrioridad(rs.getInt("IDPrioridad"));
                prioridad.setNombrePrioridad(rs.getString("NombrePrioridad"));
                prioridad.setDescripcionPrioridad(rs.getString("DescripcionPrioridad"));
                prioridades.add(prioridad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prioridades;
    }
}