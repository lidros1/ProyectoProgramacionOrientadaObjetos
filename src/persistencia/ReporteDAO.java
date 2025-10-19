// Archivo: src/persistencia/ReporteDAO.java
package persistencia;

import modelo.Proyecto;
import modelo.Tarea;
import modelo.Usuario;
import util.ConexionDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ReporteDAO {
    private static final Logger logger = Logger.getLogger(ReporteDAO.class.getName());

    // --- REPORTES DE PROYECTOS ---

    public Map<String, Integer> contarProyectosPorEstado() {
        Map<String, Integer> resultado = new HashMap<>();
        String sql = "SELECT e.NombreEstado, COUNT(p.IDProyecto) AS total " +
                "FROM proyectos p JOIN estados e ON p.IDEstado = e.IDEstado " +
                "WHERE p.Estado = 'Activo' GROUP BY e.NombreEstado";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString("NombreEstado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            logger.severe("Error al contar proyectos por estado: " + e.getMessage());
        }
        return resultado;
    }

    public Map<String, Integer> contarProyectosPorPrioridad() {
        Map<String, Integer> resultado = new HashMap<>();
        String sql = "SELECT pr.NombrePrioridad, COUNT(p.IDProyecto) AS total " +
                "FROM proyectos p JOIN prioridades pr ON p.IDPrioridad = pr.IDPrioridad " +
                "WHERE p.Estado = 'Activo' GROUP BY pr.NombrePrioridad";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString("NombrePrioridad"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            logger.severe("Error al contar proyectos por prioridad: " + e.getMessage());
        }
        return resultado;
    }

    public List<Proyecto> listarProyectosRetrasados() {
        List<Proyecto> proyectos = new ArrayList<>();
        // El estado 5 es 'HECHO'
        String sql = "SELECT p.NombreProyecto, p.FechaFinalEstimada, e.NombreEstado " +
                "FROM proyectos p JOIN estados e ON p.IDEstado = e.IDEstado " +
                "WHERE p.FechaFinalEstimada < CURDATE() AND p.IDEstado != 5 AND p.Estado = 'Activo' " +
                "ORDER BY p.FechaFinalEstimada DESC";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setNombreProyecto(rs.getString("NombreProyecto"));
                p.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                p.setNombreEstado(rs.getString("NombreEstado"));
                proyectos.add(p);
            }
        } catch (SQLException e) {
            logger.severe("Error al listar proyectos retrasados: " + e.getMessage());
        }
        return proyectos;
    }

    // --- REPORTES DE TAREAS ---

    public Map<String, Integer> contarTareasPorEstado() {
        Map<String, Integer> resultado = new HashMap<>();
        String sql = "SELECT e.NombreEstado, COUNT(t.IDTarea) AS total " +
                "FROM tareas t JOIN estados e ON t.IDEstado = e.IDEstado " +
                "WHERE t.Estado = 'Activo' GROUP BY e.NombreEstado";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString("NombreEstado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            logger.severe("Error al contar tareas por estado: " + e.getMessage());
        }
        return resultado;
    }

    public List<Tarea> listarTareasVencidas() {
        List<Tarea> tareas = new ArrayList<>();
        // El estado 5 es 'HECHO'
        String sql = "SELECT t.NombreTarea, t.FechaFinalEstimada, e.NombreEstado " +
                "FROM tareas t JOIN estados e ON t.IDEstado = e.IDEstado " +
                "WHERE t.FechaFinalEstimada < CURDATE() AND t.IDEstado != 5 AND t.Estado = 'Activo' " +
                "ORDER BY t.FechaFinalEstimada DESC";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Tarea t = new Tarea();
                t.setNombreTarea(rs.getString("NombreTarea"));
                t.setFechaFinalEstimada(rs.getDate("FechaFinalEstimada"));
                t.setNombreEstado(rs.getString("NombreEstado"));
                tareas.add(t);
            }
        } catch (SQLException e) {
            logger.severe("Error al listar tareas vencidas: " + e.getMessage());
        }
        return tareas;
    }

    // --- REPORTES DE USUARIOS ---

    public Map<String, Integer> contarTareasActivasPorUsuario() {
        Map<String, Integer> resultado = new HashMap<>();
        // Estados activos son los que no son 'HECHO' (ID 5)
        String sql = "SELECT u.nombreUsuario, COUNT(dut.IDTarea) AS total_tareas " +
                "FROM usuarios u " +
                "JOIN designacionusuariostareas dut ON u.IDUsuario = dut.IDUsuario " +
                "JOIN tareas t ON dut.IDTarea = t.IDTarea " +
                "WHERE t.IDEstado != 5 AND t.Estado = 'Activo' AND u.Estado = 'Activo' " +
                "GROUP BY u.nombreUsuario " +
                "ORDER BY total_tareas DESC";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString("nombreUsuario"), rs.getInt("total_tareas"));
            }
        } catch (SQLException e) {
            logger.severe("Error al contar tareas activas por usuario: " + e.getMessage());
        }
        return resultado;
    }

    public Map<String, Integer> contarTareasCompletadasPorUsuario() {
        Map<String, Integer> resultado = new HashMap<>();
        // El estado 5 es 'HECHO'
        String sql = "SELECT u.nombreUsuario, COUNT(dut.IDTarea) AS total_completadas " +
                "FROM usuarios u " +
                "JOIN designacionusuariostareas dut ON u.IDUsuario = dut.IDUsuario " +
                "JOIN tareas t ON dut.IDTarea = t.IDTarea " +
                "WHERE t.IDEstado = 5 AND u.Estado = 'Activo' " +
                "GROUP BY u.nombreUsuario " +
                "ORDER BY total_completadas DESC";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultado.put(rs.getString("nombreUsuario"), rs.getInt("total_completadas"));
            }
        } catch (SQLException e) {
            logger.severe("Error al contar tareas completadas por usuario: " + e.getMessage());
        }
        return resultado;
    }
}