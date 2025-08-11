package main;

import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.ItemPedido;
import model.Pedido;
import model.Producto;
import service.CategoriaServiceImpl;
import service.PedidoServiceImpl;
import service.ProductoServiceImpl;

public class main {

    public static void main(String[] args) {
        try {
            
            CategoriaServiceImpl categoriaService = new CategoriaServiceImpl(new dao.CategoriaDAOImpl());
            ProductoServiceImpl productoService = new ProductoServiceImpl();
            PedidoServiceImpl pedidoService = new PedidoServiceImpl(
                    new dao.PedidoDAOImpl(),
                    new dao.ItemPedidoDAOImpl(),
                    new dao.ProductoDAOImpl()
            );

            // 1) Crear categoría
            Categoria categoria = new Categoria(0, "Electrónica", "Productos electrónicos");
            categoriaService.crear(categoria);
            System.out.println("Categoría creada con ID: " + categoria.getId());

            // 2) Crear productos asociados a la categoría
            Producto producto1 = new Producto("Smartphone", "Teléfono inteligente", 50000.0, 10, categoria.getId());
            Producto producto2 = new Producto("Auriculares", "Auriculares inalámbricos", 15000.0, 20, categoria.getId());

            producto1 = productoService.crear(producto1);
            producto2 = productoService.crear(producto2);

            System.out.println("Producto 1 creado con ID: " + producto1.getId());
            System.out.println("Producto 2 creado con ID: " + producto2.getId());

            // 3) Crear pedido con ítems
            Pedido pedido = new Pedido();
            pedido.setFecha(new java.util.Date());
            pedido.setTotal(0);

            List<ItemPedido> items = new ArrayList<>();
            items.add(new ItemPedido(0, 0, producto1.getId(), 2, 0)); // 2 smartphones
            items.add(new ItemPedido(0, 0, producto2.getId(), 3, 0)); // 3 auriculares

            pedidoService.crearPedido(pedido, items);
            System.out.println("Pedido creado con ID: " + pedido.getId());

            // 4) Mostrar detalle del pedido
            System.out.println("\nDetalle del pedido:");
            pedidoService.mostrarDetallePedido(pedido.getId());

        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
