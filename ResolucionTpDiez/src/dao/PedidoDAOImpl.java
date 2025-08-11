package dao;

import config.DatabaseConnection;
import java.sql.*;
import java.util.*;
import model.Pedido;

public class PedidoDAOImpl implements GenericDAO<Pedido> {

    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * @param pedido Objeto Pedido con los datos a insertar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void crear(Pedido pedido, Connection conn) throws Exception {
        String sql = "INSERT INTO pedidos (fecha, total) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            stmt.setDouble(2, pedido.getTotal());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Obtiene un pedido por su ID.
     *
     * @param id Identificador único del pedido.
     * @param conn Conexión activa a la base de datos.
     * @return Pedido encontrado o null si no existe.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public Pedido leer(int id, Connection conn) throws Exception {
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pedido(
                            rs.getInt("id"),
                            rs.getDate("fecha"),
                            rs.getDouble("total")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los pedidos almacenados en la base de datos.
     *
     * @param conn Conexión activa a la base de datos.
     * @return Lista de pedidos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public List<Pedido> listar(Connection conn) throws Exception {
        String sql = "SELECT * FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pedidos.add(new Pedido(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getDouble("total")
                ));
            }
        }
        return pedidos;
    }

    /**
     * Actualiza los datos de un pedido existente.
     *
     * @param pedido Pedido con los datos actualizados (debe incluir el id).
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void actualizar(Pedido pedido, Connection conn) throws Exception {
        String sql = "UPDATE pedidos SET fecha = ?, total = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            stmt.setDouble(2, pedido.getTotal());
            stmt.setInt(3, pedido.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un pedido de la base de datos por su ID.
     *
     * @param id Identificador del pedido a eliminar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void eliminar(int id, Connection conn) throws Exception {
        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Muestra el detalle de un pedido por consola, incluyendo producto,
     * categoría, cantidad y subtotal.
     *
     * @param conn Conexión activa a la base de datos.
     * @param pedidoId ID del pedido cuyo detalle se quiere mostrar.
     * @throws SQLException Si ocurre un error durante la ejecución SQL.
     */
    public void mostrarDetallePedido(Connection conn, int pedidoId) throws SQLException {
        String sql = """
        SELECT 
            p.nombre AS producto,
            c.nombre AS categoria,
            ip.cantidad,
            ip.subtotal
        FROM pedidos pe
        JOIN items_pedido ip ON pe.id = ip.pedido_id
        JOIN productos p ON ip.producto_id = p.id
        JOIN categorias c ON p.id_categoria = c.id
        WHERE pe.id = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String producto = rs.getString("producto");
                    String categoria = rs.getString("categoria");
                    int cantidad = rs.getInt("cantidad");
                    double subtotal = rs.getDouble("subtotal");

                    System.out.println("Producto=" + producto
                            + ", Categoría=" + categoria
                            + ", Cantidad=" + cantidad
                            + ", Subtotal=" + subtotal);
                }
            }
        }
    }

}
