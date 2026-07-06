package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión Singleton a MySQL.
 * Coloca este archivo en src/controlador/
 */
public class ConexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/hotel_trugarden?useSSL=false&serverTimezone=America/Lima";
    private static final String USUARIO  = "root";       // cambia si tienes otro usuario
    private static final String CLAVE    = "123456";   // pon tu clave de MySQL aquí

    private static Connection conexion = null;

    private ConexionDB() {}

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
                System.out.println("Conexion MySQL exitosa.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el driver MySQL. ¿Agregaste el JAR?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR al conectar con MySQL: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexion MySQL cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
