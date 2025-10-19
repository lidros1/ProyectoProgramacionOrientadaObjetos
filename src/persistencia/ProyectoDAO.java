package persistencia;

import modelo.Proyecto;
import util.ConexionDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProyectoDAO {
    private static final Logger logger = Logger.getLogger(ProyectoDAO.class.getName());

    public List<Proyecto> listarProyectosPorUsuario(int idUsuario) {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.IDProyecto, p.NombreProyecto, p.DescripcionProyecto, " +
                "p.ProcentajeAvance, p.FechaInicio, p.FechaFinalEstimada, " +
                "e.NombreEstado, pr.NombrePrioridad, dp.IDJerarquiaUsuario " +
                "FROM proyectos p " +
                "JOIN designacionproyectos dp ON p.IDProyecto = dp.IDProyecto " +
                "JOIN estados e ON p.IDEstado = e.IDEstado " +
                "JOIN prioridades pr ON p.IDPrioridad = pr.IDPrioridad " +
                "WHERE dp.IDUsuario = ? AND p.Estado = 'Activo'";

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Proyecto proyecto = new Proyecto();
                    proyecto.setIdProyecto(rs.getInt("IDProyecto"));
                    proyecto.setNombreProyecto(rs.getString("NombreProyecto"));
                    proyecto.setDescripcionProyecto(rs.getString("DescripcionProyecto"));
                    proyecto.setPorcentajeAvance(rs.getBigDecimal("ProcentajeAvance"));
                    proyecto.setFechaInicio(rs.getDate("FechaInicio"));
                    proyecto.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                    proyecto.setNombreEstado(rs.getString("NombreEstado"));
                    proyecto.setNombrePrioridad(rs.getString("NombrePrioridad"));
                    proyecto.setIdJerarquiaUsuario(rs.getInt("IDJerarquiaUsuario"));
                    proyectos.add(proyecto);
                }
            }
        } catch (SQLException e) {
            logger.warning("Error al listar proyectos: " + e.getMessage());
        }
        return proyectos;
    }

    public boolean actualizarEstado(int idProyecto, int idNuevoEstado) {
        String sql = "UPDATE proyectos SET IDEstado = ? WHERE IDProyecto = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idNuevoEstado);
            pstmt.setInt(2, idProyecto);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.warning("Error al actualizar estado de proyecto: " + e.getMessage());
            return false;
        }
    }

    public int insertarYObtenerId(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (NombreProyecto, DescripcionProyecto, FechaInicio, FechaFinalEstimada, IDEstado, IDPrioridad, ProcentajeAvance, Estado) VALUES (?, ?, ?, ?, ?, ?, 0.00, 'Activo')";
        int idEstadoPorHacer = 2;

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, proyecto.getNombreProyecto());
            pstmt.setString(2, proyecto.getDescripcionProyecto());
            pstmt.setDate(3, proyecto.getFechaInicio() != null ? new java.sql.Date(proyecto.getFechaInicio().getTime()) : null);
            pstmt.setDate(4, proyecto.getFechaFinalEstimada() != null ? new java.sql.Date(proyecto.getFechaFinalEstimada().getTime()) : null);
            pstmt.setInt(5, idEstadoPorHacer);
            pstmt.setInt(6, proyecto.getIdPrioridad());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al insertar proyecto y obtener ID: " + e.getMessage());
        }
        return -1;
    }

    public boolean insertarDesignacionProyecto(int idUsuario, int idProyecto, int idJerarquiaUsuario) {
        String sql = "INSERT INTO designacionproyectos (IDUsuario, IDProyecto, IDJerarquiaUsuario, Estado) VALUES (?, ?, ?, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idProyecto);
            pstmt.setInt(3, idJerarquiaUsuario);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.severe("Error al insertar designación de proyecto: " + e.getMessage());
            return false;
        }
    }

    public java.math.BigDecimal calcularPorcentajeAvance(int idProyecto) {
        String sql = "SELECT " +
                "  (COUNT(CASE WHEN e.NombreEstado = 'HECHO' THEN 1 END) * 100.0 / COUNT(t.IDTarea)) " +
                "FROM tareas t " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "WHERE t.IDProyecto = ?";

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProyecto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    java.math.BigDecimal porcentaje = rs.getBigDecimal(1);
                    return (porcentaje == null) ? java.math.BigDecimal.ZERO : porcentaje;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al calcular el porcentaje de avance del proyecto: " + e.getMessage());
        }
        return java.math.BigDecimal.ZERO;
    }

    public List<Proyecto> listarTodosLosProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.IDProyecto, p.NombreProyecto, p.FechaInicio, p.FechaFinalEstimada, pr.NombrePrioridad, e.NombreEstado " +
                "FROM proyectos p " +
                "JOIN prioridades pr ON p.IDPrioridad = pr.IDPrioridad " +
                "JOIN estados e ON p.IDEstado = e.IDEstado " +
                "WHERE p.Estado = 'Activo'";

        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(rs.getInt("IDProyecto"));
                proyecto.setNombreProyecto(rs.getString("NombreProyecto"));
                proyecto.setNombrePrioridad(rs.getString("NombrePrioridad"));
                proyecto.setNombreEstado(rs.getString("NombreEstado"));
                proyecto.setFechaInicio(rs.getDate("FechaInicio"));
                proyecto.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));

                java.math.BigDecimal avance = calcularPorcentajeAvance(proyecto.getIdProyecto());
                proyecto.setPorcentajeAvance(avance);

                proyectos.add(proyecto);
            }
        } catch (SQLException e) {
            logger.warning("Error al listar todos los proyectos: " + e.getMessage());
        }
        return proyectos;
    }

    /**
     * Actualiza los datos principales de un proyecto.
     * @param proyecto El objeto Proyecto con los datos a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizar(Proyecto proyecto) {
        // --- CONSULTA SQL MODIFICADA ---
        String sql = "UPDATE proyectos SET NombreProyecto = ?, IDEstado = ?, IDPrioridad = ?, " +
                "FechaInicio = ?, FechaFinalEstimada = ? WHERE IDProyecto = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, proyecto.getNombreProyecto());
            pstmt.setInt(2, proyecto.getIdEstado());
            pstmt.setInt(3, proyecto.getIdPrioridad());
            // --- NUEVOS PARÁMETROS ---
            pstmt.setDate(4, proyecto.getFechaInicio() != null ? new java.sql.Date(proyecto.getFechaInicio().getTime()) : null);
            pstmt.setDate(5, proyecto.getFechaFinalEstimada() != null ? new java.sql.Date(proyecto.getFechaFinalEstimada().getTime()) : null);
            // --- ID DEL PROYECTO ---
            pstmt.setInt(6, proyecto.getIdProyecto());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.severe("Error al actualizar el proyecto: " + e.getMessage());
            return false;
        }
    }
}