package persistencia;

import modelo.JerarquiaUsuario;
import util.ConexionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JerarquiaUsuarioDAO {
    public JerarquiaUsuario obtenerPorId(int idJerarquia) {
        // La consulta SQL se actualiza para usar los nombres corregidos de la tabla y las columnas.
        String sql = "SELECT IDJerarquiaUsuario, NombreJerarquia, DescripcionJerarquia FROM jerarquiausuarios WHERE IDJerarquiaUsuario = ? AND Estado = 'Activo'";
        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJerarquia);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Se corrige el nombre de la variable y las llamadas a los métodos/columnas.
                    JerarquiaUsuario jerarquia = new JerarquiaUsuario();
                    jerarquia.setIdJerarquiaUsuario(rs.getInt("IDJerarquiaUsuario"));
                    jerarquia.setNombreJerarquia(rs.getString("NombreJerarquia"));
                    jerarquia.setDescripcionJerarquia(rs.getString("DescripcionJerarquia"));
                    return jerarquia;
                }
            }
        } catch (SQLException e) {
            // Se recomienda un manejo de excepciones más robusto que solo imprimir la traza.
            e.printStackTrace();
        }
        return null;
    }
}