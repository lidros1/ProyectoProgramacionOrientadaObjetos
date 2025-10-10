import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDataBase {

    // --- DATOS DE TU BASE DE DATOS ---
    // Modifica estos valores con los de tu propia base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/gestorproyectos";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Método para obtener una conexión a la base de datos.
     * @return Objeto de conexión a la BD o null si hay un error.
     */
    public static Connection getConnection() {
        Connection connection = null;

        try {
            // Cargar el driver de MySQL
            // (En versiones modernas de JDBC, este paso es a menudo opcional)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión a la base de datos exitosa!");

        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectarse a la base de datos.");
            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args) {
        // Ejemplo de cómo usar el método
        Connection conn = getConnection();

        if (conn != null) {
            // Aquí puedes empezar a realizar operaciones con la base de datos
            // (ejecutar consultas, etc.)
            try {
                // No olvides cerrar la conexión cuando termines
                conn.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}