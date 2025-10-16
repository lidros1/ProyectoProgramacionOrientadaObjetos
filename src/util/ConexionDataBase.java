// Archivo: src/util/ConexionDataBase.java
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDataBase {

    // --- DATOS DE TU BASE DE DATOS ---
    // Modifica USER y PASSWORD si son diferentes en tu PC
    private static final String URL = "jdbc:mysql://localhost:3306/gestorproyectos";
    private static final String USER = "root"; // cámbialo por tu usuario
    private static final String PASSWORD = ""; // cámbialo por tu contraseña

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectarse a la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }
}