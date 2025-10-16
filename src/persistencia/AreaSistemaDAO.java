package persistencia;

import modelo.AreaSistema;
import util.ConexionDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AreaSistemaDAO {
    private static final Logger logger = Logger.getLogger(AreaSistemaDAO.class.getName());

    public List<AreaSistema> listarTodas() {
        List<AreaSistema> areas = new ArrayList<>();
        String sql = "SELECT IDAreaSistema, nombreAreaSistema FROM areassistema WHERE Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AreaSistema area = new AreaSistema();
                area.setIdAreaSistema(rs.getInt("IDAreaSistema"));
                area.setNombreAreaSistema(rs.getString("nombreAreaSistema"));
                areas.add(area);
            }
        } catch (SQLException e) {
            logger.severe("Error al listar áreas del sistema: " + e.getMessage());
        }
        return areas;
    }
}