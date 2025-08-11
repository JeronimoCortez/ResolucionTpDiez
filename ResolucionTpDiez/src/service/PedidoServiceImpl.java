package service;

import dao.PedidoDAOImpl;
import dao.ProductoDAOImpl;
import model.ItemPedido;
import model.Pedido;
import model.Producto;
import java.sql.Connection;
import config.DatabaseConnection;
import dao.ItemPedidoDAOImpl;
import java.util.List;

public class PedidoServiceImpl {

    private final PedidoDAOImpl pedidoDAO;
    private final ItemPedidoDAOImpl itemPedidoDAO;
    private final ProductoDAOImpl productoDAO;

    public PedidoServiceImpl(PedidoDAOImpl pedidoDAO, ItemPedidoDAOImpl itemPedidoDAO, ProductoDAOImpl productoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.itemPedidoDAO = itemPedidoDAO;
        this.productoDAO = productoDAO;
    }

    /**
     * Crea un nuevo pedido junto con sus items, validando stock y actualizando
     * cantidades. La operación se realiza en una transacción para asegurar la
     * consistencia.
     *
     * @param pedido Pedido a crear (el total será calculado e actualizado).
     * @param items Lista de items que pertenecen al pedido.
     * @throws Exception Si algún producto no existe, no tiene stock suficiente
     * o ocurre un error en la base de datos.
     */
    public void crearPedido(Pedido pedido, List<ItemPedido> items) throws Exception {
        Connection conn = null;
        try {
            conn = config.DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Validar stock para todos los items
            for (ItemPedido item : items) {
                Producto producto = productoDAO.leer(item.getIdProducto(), conn);
                if (producto == null) {
                    throw new RuntimeException("Producto no encontrado ID " + item.getIdProducto());
                }
                if (producto.getCantidad() < item.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
                }
            }

            // Crear pedido (total inicial 0, luego actualizamos)
            pedidoDAO.crear(pedido, conn);

            double totalPedido = 0;

            // Crear items, calcular subtotal y actualizar stock
            for (ItemPedido item : items) {
                Producto producto = productoDAO.leer(item.getIdProducto(), conn);

                item.setIdPedido(pedido.getId());
                item.setSubtotal(producto.getPrecio() * item.getCantidad());
                totalPedido += item.getSubtotal();

                itemPedidoDAO.crear(item, conn);

                // Actualizar stock producto
                producto.setCantidad(producto.getCantidad() - item.getCantidad());
                productoDAO.actualizar(producto, conn);
            }

            // Actualizar total del pedido
            pedido.setTotal(totalPedido);
            pedidoDAO.actualizar(pedido, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Muestra el detalle completo de un pedido por su ID, incluyendo
     * información general y detalle de items. Si el pedido no existe, imprime
     * un mensaje indicando que no fue encontrado.
     *
     * @param pedidoId ID del pedido a mostrar.
     * @throws Exception Si ocurre un error al acceder a la base de datos.
     */
    public void mostrarDetallePedido(int pedidoId) throws Exception {
        try (Connection conn = config.DatabaseConnection.getConnection()) {
            Pedido pedido = pedidoDAO.leer(pedidoId, conn);
            if (pedido == null) {
                System.out.println("Pedido no encontrado con ID " + pedidoId);
                return;
            }
            System.out.println("Pedido ID: " + pedido.getId() + ", Fecha: " + pedido.getFecha() + ", Total: " + pedido.getTotal());
            pedidoDAO.mostrarDetallePedido(conn, pedidoId);
        }
    }
}
