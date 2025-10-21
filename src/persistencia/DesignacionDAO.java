// Archivo: src/persistencia/DesignacionDAO.java
package persistencia;

import modelo.Usuario;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DesignacionDAO {
    private static final Logger logger = Logger.getLogger(DesignacionDAO.class.getName());

    public List<Usuario> listarUsuariosDesignadosAProyecto(int idProyecto) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.IDUsuario, u.nombreUsuario " +
                "FROM designacionproyectos dp " +
                "JOIN usuarios u ON dp.IDUsuario = u.IDUsuario " +
                "WHERE dp.IDProyecto = ? AND dp.Estado = 'Activo' AND u.Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProyecto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al listar usuarios designados a proyecto: " + e.getMessage());
        }
        return usuarios;
    }

    public List<Usuario> listarUsuariosDesignadosATarea(int idTarea) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.IDUsuario, u.nombreUsuario " +
                "FROM designacionusuariostareas dut " +
                "JOIN usuarios u ON dut.IDUsuario = u.IDUsuario " +
                "WHERE dut.IDTarea = ? AND dut.Estado = 'Activo' AND u.Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al listar usuarios designados a tarea: " + e.getMessage());
        }
        return usuarios;
    }

    public boolean eliminarDesignacionesPorProyecto(int idProyecto) {
        String sql = "DELETE FROM designacionproyectos WHERE IDProyecto = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProyecto);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.severe("Error al eliminar designaciones por proyecto: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarDesignaciones(int idProyecto, List<Usuario> usuarios) {
        String sql = "INSERT INTO designacionproyectos (IDProyecto, IDUsuario, IDJerarquiaUsuario) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            int idJerarquiaDeveloper = 3;
            for (Usuario usuario : usuarios) {
                pstmt.setInt(1, idProyecto);
                pstmt.setInt(2, usuario.getIdUsuario());
                pstmt.setInt(3, idJerarquiaDeveloper);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            logger.severe("Error al insertar designaciones en lote: " + e.getMessage());
            return false;
        }
    }

    public List<String> listarNombresJerarquiaPorUsuario(int idUsuario) {
        List<String> jerarquias = new ArrayList<>();
        String sql = "SELECT DISTINCT ju.NombreJerarquia " +
                "FROM designacionproyectos dp " +
                "JOIN jerarquiausuarios ju ON dp.IDJerarquiaUsuario = ju.IDJerarquiaUsuario " +
                "WHERE dp.IDUsuario = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jerarquias.add(rs.getString("NombreJerarquia"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al listar jerarquías por usuario: " + e.getMessage());
        }
        return jerarquias;
    }

    public boolean eliminarDesignacionesPorTarea(int idTarea) {
        String sql = "DELETE FROM designacionusuariostareas WHERE IDTarea = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.severe("Error al eliminar designaciones por tarea: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarDesignacionesTarea(int idTarea, List<Usuario> usuarios) {
        String sql = "INSERT INTO designacionusuariostareas (IDTarea, IDUsuario) VALUES (?, ?)";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Usuario usuario : usuarios) {
                pstmt.setInt(1, idTarea);
                pstmt.setInt(2, usuario.getIdUsuario());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            logger.severe("Error al insertar designaciones de tarea en lote: " + e.getMessage());
            return false;
        }
    }
}