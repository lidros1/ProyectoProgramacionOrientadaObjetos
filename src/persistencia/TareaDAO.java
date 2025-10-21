// Archivo: src/persistencia/TareaDAO.java
package persistencia;

import modelo.Tarea;
import util.ConexionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TareaDAO {
    private static final Logger logger = Logger.getLogger(TareaDAO.class.getName());

    private final String casePorcentajeAvance =
            "CASE e.NombreEstado " +
                    "  WHEN 'HECHO' THEN 100 " +
                    "  WHEN 'EN REVISIÓN' THEN 70 " +
                    "  WHEN 'EN PROGRESO' THEN 30 " +
                    "  WHEN 'BLOQUEADO' THEN 30 " +
                    "  ELSE 0 " +
                    "END as PorcentajeCalculado";

    public List<Tarea> listarTareasPorProyectoYUsuario(int idProyecto, int idUsuario) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.IDTarea, t.NombreTarea, " + casePorcentajeAvance + ", t.FechaInicio, t.FechaFinalEstimada, " +
                "e.NombreEstado, p.NombrePrioridad, t.IDProyecto " +
                "FROM tareas t " +
                "JOIN designacionusuariostareas dut ON t.IDTarea = dut.IDTarea " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "JOIN prioridades p ON t.IDPrioridad = p.IDPrioridad " +
                "WHERE t.IDProyecto = ? AND dut.IDUsuario = ? AND t.Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProyecto);
            pstmt.setInt(2, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setIdTarea(rs.getInt("IDTarea"));
                    tarea.setIdProyecto(rs.getInt("IDProyecto"));
                    tarea.setNombreTarea(rs.getString("NombreTarea"));
                    tarea.setPorcentajeAvance(rs.getBigDecimal("PorcentajeCalculado"));
                    tarea.setFechaInicio(rs.getDate("FechaInicio"));
                    tarea.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                    tarea.setNombreEstado(rs.getString("NombreEstado"));
                    tarea.setNombrePrioridad(rs.getString("NombrePrioridad"));
                    tareas.add(tarea);
                }
            }
        } catch (SQLException e) {
            logger.warning("Error al listar tareas por proyecto y usuario: " + e.getMessage());
        }
        return tareas;
    }

    public List<Tarea> listarTareasPorProyecto(int idProyecto) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.IDTarea, t.NombreTarea, " + casePorcentajeAvance + ", t.FechaInicio, t.FechaFinalEstimada, " +
                "e.NombreEstado, p.NombrePrioridad, t.IDProyecto, t.IDEstado, t.IDPrioridad, t.Estado " +
                "FROM tareas t " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "JOIN prioridades p ON t.IDPrioridad = p.IDPrioridad " +
                "WHERE t.IDProyecto = ?"; // Mostramos todas para poder editarlas
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProyecto);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setIdTarea(rs.getInt("IDTarea"));
                    tarea.setIdProyecto(rs.getInt("IDProyecto"));
                    tarea.setIdEstado(rs.getInt("IDEstado"));
                    tarea.setIdPrioridad(rs.getInt("IDPrioridad"));
                    tarea.setNombreTarea(rs.getString("NombreTarea"));
                    tarea.setPorcentajeAvance(rs.getBigDecimal("PorcentajeCalculado"));
                    tarea.setFechaInicio(rs.getDate("FechaInicio"));
                    tarea.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                    tarea.setNombreEstado(rs.getString("NombreEstado"));
                    tarea.setNombrePrioridad(rs.getString("NombrePrioridad"));
                    tarea.setEstado(rs.getString("Estado"));
                    tareas.add(tarea);
                }
            }
        } catch (SQLException e) {
            logger.warning("Error al listar todas las tareas por proyecto: " + e.getMessage());
        }
        return tareas;
    }

    public boolean actualizarEstado(int idTarea, int idNuevoEstado) {
        String sql = "UPDATE tareas SET IDEstado = ? WHERE IDTarea = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idNuevoEstado);
            pstmt.setInt(2, idTarea);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.warning("Error al actualizar estado de tarea: " + e.getMessage());
            return false;
        }
    }

    public List<Tarea> listarTodasTareasPorUsuario(int idUsuario) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.IDTarea, t.NombreTarea, " + casePorcentajeAvance + ", t.FechaInicio, t.FechaFinalEstimada, " +
                "e.NombreEstado, p.NombrePrioridad, t.IDProyecto, t.IDEstado, t.IDPrioridad, t.Estado " +
                "FROM tareas t " +
                "JOIN designacionusuariostareas dut ON t.IDTarea = dut.IDTarea " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "JOIN prioridades p ON t.IDPrioridad = p.IDPrioridad " +
                "WHERE dut.IDUsuario = ? AND t.Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setIdTarea(rs.getInt("IDTarea"));
                    tarea.setIdProyecto(rs.getInt("IDProyecto"));
                    tarea.setIdEstado(rs.getInt("IDEstado"));
                    tarea.setIdPrioridad(rs.getInt("IDPrioridad"));
                    tarea.setNombreTarea(rs.getString("NombreTarea"));
                    tarea.setPorcentajeAvance(rs.getBigDecimal("PorcentajeCalculado"));
                    tarea.setFechaInicio(rs.getDate("FechaInicio"));
                    tarea.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                    tarea.setNombreEstado(rs.getString("NombreEstado"));
                    tarea.setNombrePrioridad(rs.getString("NombrePrioridad"));
                    tarea.setEstado(rs.getString("Estado"));
                    tareas.add(tarea);
                }
            }
        } catch (SQLException e) {
            logger.warning("Error al listar todas las tareas por usuario: " + e.getMessage());
        }
        return tareas;
    }

    public Tarea obtenerTareaCompletaPorId(int idTarea) {
        Tarea tarea = null;
        String sql = "SELECT t.IDTarea, t.IDProyecto, t.IDEstado, t.IDPrioridad, t.NombreTarea, " +
                casePorcentajeAvance + ", t.FechaInicio, t.FechaFinalEstimada, t.Estado as TareaEstado, " +
                "e.NombreEstado, p.NombrePrioridad " +
                "FROM tareas t " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "JOIN prioridades p ON t.IDPrioridad = p.IDPrioridad " +
                "WHERE t.IDTarea = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tarea = new Tarea();
                    tarea.setIdTarea(rs.getInt("IDTarea"));
                    tarea.setIdProyecto(rs.getInt("IDProyecto"));
                    tarea.setIdEstado(rs.getInt("IDEstado"));
                    tarea.setIdPrioridad(rs.getInt("IDPrioridad"));
                    tarea.setNombreTarea(rs.getString("NombreTarea"));
                    tarea.setPorcentajeAvance(rs.getBigDecimal("PorcentajeCalculado"));
                    tarea.setFechaInicio(rs.getDate("FechaInicio"));
                    tarea.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                    tarea.setEstado(rs.getString("TareaEstado"));
                    tarea.setNombreEstado(rs.getString("NombreEstado"));
                    tarea.setNombrePrioridad(rs.getString("NombrePrioridad"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al obtener tarea completa por ID: " + e.getMessage());
        }
        return tarea;
    }

    public boolean actualizar(Tarea tarea) {
        String sql = "UPDATE tareas SET " +
                "NombreTarea = ?, " +
                "FechaInicio = ?, " +
                "FechaFinalEstimada = ?, " +
                "IDEstado = ?, " +
                "IDPrioridad = ?, " +
                "Estado = ? " +
                "WHERE IDTarea = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tarea.getNombreTarea());
            pstmt.setDate(2, tarea.getFechaInicio() != null ? new java.sql.Date(tarea.getFechaInicio().getTime()) : null);
            pstmt.setDate(3, tarea.getFechaFinalEstimada() != null ? new java.sql.Date(tarea.getFechaFinalEstimada().getTime()) : null);
            pstmt.setInt(4, tarea.getIdEstado());
            pstmt.setInt(5, tarea.getIdPrioridad());
            pstmt.setString(6, tarea.getEstado());
            pstmt.setInt(7, tarea.getIdTarea());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.severe("Error al actualizar la tarea: " + e.getMessage());
            return false;
        }
    }

    public int insertarYObtenerId(Tarea tarea) {
        String sql = "INSERT INTO tareas (IDProyecto, IDEstado, IDPrioridad, NombreTarea, FechaInicio, FechaFinalEstimada, ProcentajeAvance, Estado) " +
                "VALUES (?, 2, ?, ?, ?, ?, 0.00, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tarea.getIdProyecto());
            pstmt.setInt(2, tarea.getIdPrioridad());
            pstmt.setString(3, tarea.getNombreTarea());
            pstmt.setDate(4, tarea.getFechaInicio() != null ? new java.sql.Date(tarea.getFechaInicio().getTime()) : null);
            pstmt.setDate(5, tarea.getFechaFinalEstimada() != null ? new java.sql.Date(tarea.getFechaFinalEstimada().getTime()) : null);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al insertar tarea y obtener ID: " + e.getMessage());
        }
        return -1;
    }

    public List<Tarea> listarTodasLasTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.IDTarea, t.NombreTarea, " + casePorcentajeAvance + ", t.FechaInicio, t.FechaFinalEstimada, " +
                "e.NombreEstado, p.NombrePrioridad, t.IDProyecto, t.IDEstado, t.IDPrioridad, t.Estado " +
                "FROM tareas t " +
                "JOIN estados e ON t.IDEstado = e.IDEstado " +
                "JOIN prioridades p ON t.IDPrioridad = p.IDPrioridad";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setIdTarea(rs.getInt("IDTarea"));
                tarea.setIdProyecto(rs.getInt("IDProyecto"));
                tarea.setIdEstado(rs.getInt("IDEstado"));
                tarea.setIdPrioridad(rs.getInt("IDPrioridad"));
                tarea.setNombreTarea(rs.getString("NombreTarea"));
                tarea.setPorcentajeAvance(rs.getBigDecimal("PorcentajeCalculado"));
                tarea.setFechaInicio(rs.getDate("FechaInicio"));
                tarea.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                tarea.setNombreEstado(rs.getString("NombreEstado"));
                tarea.setNombrePrioridad(rs.getString("NombrePrioridad"));
                tarea.setEstado(rs.getString("Estado"));
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            logger.warning("Error al listar todas las tareas: " + e.getMessage());
        }
        return tareas;
    }
}