package dao;

import java.util.List;
import model.ItemPedido;
import java.sql.*;
import java.util.*;

public class ItemPedidoDAOImpl implements GenericDAO<ItemPedido> {

    /**
     * Inserta un nuevo item de pedido en la base de datos.
     *
     * @param item Objeto ItemPedido con los datos a insertar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void crear(ItemPedido item, Connection conn) throws Exception {
        String sql = "INSERT INTO items_pedido (pedido_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdProducto());
            stmt.setInt(3, item.getCantidad());
            stmt.setDouble(4, item.getSubtotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Obtiene un item de pedido por su ID.
     *
     * @param id Identificador único del item de pedido.
     * @param conn Conexión activa a la base de datos.
     * @return ItemPedido encontrado o null si no existe.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public ItemPedido leer(int id, Connection conn) throws Exception {
        String sql = "SELECT * FROM items_pedido WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ItemPedido(
                            rs.getInt("id"),
                            rs.getInt("id_pedido"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("subtotal")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los items de pedido existentes en la base de datos.
     *
     * @param conn Conexión activa a la base de datos.
     * @return Lista de ItemPedido.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public List<ItemPedido> listar(Connection conn) throws Exception {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ItemPedido(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                ));
            }
        }
        return lista;
    }

    /**
     * Actualiza los datos de un item de pedido existente.
     *
     * @param item ItemPedido con los datos actualizados (debe incluir el id).
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void actualizar(ItemPedido item, Connection conn) throws Exception {
        String sql = "UPDATE items_pedido SET id_pedido = ?, id_producto = ?, cantidad = ?, subtotal = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdProducto());
            stmt.setInt(3, item.getCantidad());
            stmt.setDouble(4, item.getSubtotal());
            stmt.setInt(5, item.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un item de pedido de la base de datos por su ID.
     *
     * @param id Identificador del item de pedido a eliminar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error durante la ejecución SQL.
     */
    @Override
    public void eliminar(int id, Connection conn) throws Exception {
        String sql = "DELETE FROM items_pedido WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
