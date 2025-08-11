package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3307/db"); // Seteamos la url de nuestra BD
        config.setUsername("root"); // Usuario BD
        config.setPassword(""); // Password BD
        config.setMaximumPoolSize(10);  // Maximo 10 conexiones
        ds = new HikariDataSource(config);  // Inicialización del datasource con la configuración anterior
    }

    /**
     * Obtiene una conexión activa del pool de conexiones.
     *
     * @return Conexión a la base de datos.
     * @throws SQLException Si ocurre un error al obtener la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
