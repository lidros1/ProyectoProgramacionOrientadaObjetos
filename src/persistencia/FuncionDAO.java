package persistencia;

import modelo.Funcion;
import util.ConexionDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FuncionDAO {
    private static final Logger logger = Logger.getLogger(FuncionDAO.class.getName());

    public List<Funcion> listarTodas() {
        List<Funcion> funciones = new ArrayList<>();
        String sql = "SELECT IDFuncion, nombreFuncion FROM funciones WHERE Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcion funcion = new Funcion();
                funcion.setIdFuncion(rs.getInt("IDFuncion"));
                funcion.setNombreFuncion(rs.getString("nombreFuncion"));
                funciones.add(funcion);
            }
        } catch (SQLException e) {
            logger.severe("Error al listar funciones: " + e.getMessage());
        }
        return funciones;
    }
}