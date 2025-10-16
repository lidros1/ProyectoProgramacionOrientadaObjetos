package persistencia;

import modelo.Usuario;
import util.ConexionDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT IDUsuario, nombreUsuario, Mail, Estado FROM usuarios WHERE Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("IDUsuario"));
                usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                usuario.setMail(rs.getString("Mail"));
                usuario.setEstado(rs.getString("Estado"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            logger.severe("Error al listar todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombreUsuario, contraseñaUsuario, Mail, Estado) VALUES (?, ?, ?, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasena());
            pstmt.setString(3, usuario.getMail());
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            logger.severe("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public int insertarYObtenerId(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombreUsuario, contraseñaUsuario, Mail, Estado) VALUES (?, ?, ?, 'Activo')";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasena());
            pstmt.setString(3, usuario.getMail());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al insertar usuario y obtener ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * MODIFICADO: Actualiza los datos de un usuario.
     * Si la contraseña en el objeto usuario está vacía o es nula, no se actualiza.
     */
    public boolean actualizar(Usuario usuario) {
        // Construcción dinámica de la consulta SQL
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET nombreUsuario = ?, Mail = ?");
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            sql.append(", contraseñaUsuario = ?");
        }
        sql.append(" WHERE IDUsuario = ?");

        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getMail());

            int parameterIndex = 3;
            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                pstmt.setString(parameterIndex, usuario.getContrasena());
                parameterIndex++;
            }
            pstmt.setInt(parameterIndex, usuario.getIdUsuario());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            logger.severe("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "UPDATE usuarios SET Estado = 'Inactivo' WHERE IDUsuario = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            logger.severe("Error al eliminar (desactivar) usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario validarUsuario(String nombreUsuario, String contraseña) {
        String sql = "SELECT IDUsuario, nombreUsuario, Mail, Estado FROM usuarios WHERE nombreUsuario = ? AND contraseñaUsuario = ? AND Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contraseña);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                    usuario.setMail(rs.getString("Mail"));
                    usuario.setEstado(rs.getString("Estado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al validar usuario: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarFechaUltimoAcceso(int idUsuario) {
        String sql = "UPDATE usuarios SET fechaUltimoAccesoUsuario = NOW() WHERE IDUsuario = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.severe("Error al actualizar fecha de último acceso para el usuario " + idUsuario + ": " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT IDUsuario, nombreUsuario FROM usuarios WHERE nombreUsuario LIKE ? AND Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al buscar usuarios por nombre: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario obtenerUsuarioCompletoPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE IDUsuario = ?";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                    usuario.setMail(rs.getString("Mail"));
                    usuario.setFechaCreacion(rs.getTimestamp("fechaCreacionUsuario"));
                    usuario.setFechaUltimoAcceso(rs.getTimestamp("fechaUltimoAccesoUsuario"));
                    usuario.setEstado(rs.getString("Estado"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al obtener usuario completo por ID: " + e.getMessage());
        }
        return usuario;
    }
}