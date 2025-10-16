package persistencia;

import modelo.Permiso;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PermisoDAO {
    private static final Logger logger = Logger.getLogger(PermisoDAO.class.getName());

    public List<Permiso> listarPermisosPorUsuario(int idUsuario) {
        List<Permiso> permisos = new ArrayList<>();
        String sql = "SELECT pu.IDPermisoUsuario, pu.IDUsuario, pu.IDAreaSistema, asis.nombreAreaSistema, pu.IDFuncion, f.nombreFuncion, pu.Estado " +
                "FROM permisosusuarios pu " +
                "JOIN areassistema asis ON pu.IDAreaSistema = asis.IDAreaSistema " +
                "JOIN funciones f ON pu.IDFuncion = f.IDFuncion " +
                "WHERE pu.IDUsuario = ? AND pu.Estado = 'Activo'";

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Permiso permiso = new Permiso();
                    permiso.setIdPermisoUsuario(rs.getInt("IDPermisoUsuario"));
                    permiso.setIdUsuario(rs.getInt("IDUsuario"));
                    permiso.setIdAreaSistema(rs.getInt("IDAreaSistema"));
                    permiso.setNombreArea(rs.getString("nombreAreaSistema"));
                    permiso.setIdFuncion(rs.getInt("IDFuncion"));
                    permiso.setNombreFuncion(rs.getString("nombreFuncion"));
                    permiso.setEstado(rs.getString("Estado"));
                    permisos.add(permiso);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al listar permisos por usuario: " + e.getMessage());
        }
        return permisos;
    }

    public boolean insertarPermisos(int idUsuario, List<Permiso> permisos) {
        String sql = "INSERT INTO permisosusuarios (IDUsuario, IDAreaSistema, IDFuncion, Estado) VALUES (?, ?, ?, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Permiso permiso : permisos) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, permiso.getIdAreaSistema());
                pstmt.setInt(3, permiso.getIdFuncion());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
            return true;

        } catch (SQLException e) {
            logger.severe("Error al insertar permisos en lote: " + e.getMessage());
            try (Connection conn = ConexionDataBase.getConnection()) {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        }
    }

    /**
     * NUEVO MÉTODO: Elimina todos los permisos asociados a un ID de usuario.
     * @param idUsuario El ID del usuario cuyos permisos serán eliminados.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPermisosPorUsuario(int idUsuario) {
        String sql = "DELETE FROM permisosusuarios WHERE IDUsuario = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.severe("Error al eliminar permisos por usuario: " + e.getMessage());
            return false;
        }
    }
}