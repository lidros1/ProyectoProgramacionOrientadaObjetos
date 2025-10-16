package persistencia;

import modelo.Comentario;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ComentarioDAO {
    private static final Logger logger = Logger.getLogger(ComentarioDAO.class.getName());

    public List<Comentario> listarComentariosPorTarea(int idTarea) {
        List<Comentario> comentarios = new ArrayList<>();
        String sql = "SELECT c.IDComentario, c.IDTarea, c.IDUsuario, u.nombreUsuario, c.Contenido, c.FechaCreacion, c.Estado " +
                "FROM comentarios c " +
                "JOIN usuarios u ON c.IDUsuario = u.IDUsuario " +
                "WHERE c.IDTarea = ? AND c.Estado = 'Activo' " +
                "ORDER BY c.FechaCreacion ASC";

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idTarea);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comentario comentario = new Comentario();
                    comentario.setIdComentario(rs.getInt("IDComentario"));
                    comentario.setIdTarea(rs.getInt("IDTarea"));
                    comentario.setIdUsuario(rs.getInt("IDUsuario"));
                    comentario.setNombreUsuarioComentario(rs.getString("nombreUsuario"));
                    comentario.setContenido(rs.getString("Contenido"));
                    comentario.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                    comentario.setEstado(rs.getString("Estado"));
                    comentarios.add(comentario);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al listar comentarios por tarea: " + e.getMessage());
        }
        return comentarios;
    }

    /**
     * Inserta un nuevo comentario en la base de datos.
     * @param comentario El objeto Comentario a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertar(Comentario comentario) {
        String sql = "INSERT INTO comentarios (IDTarea, IDUsuario, Contenido, Estado) VALUES (?, ?, ?, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, comentario.getIdTarea());
            pstmt.setInt(2, comentario.getIdUsuario());
            pstmt.setString(3, comentario.getContenido());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.severe("Error al insertar comentario: " + e.getMessage());
            return false;
        }
    }
}