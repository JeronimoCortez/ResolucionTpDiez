package service;

import config.DatabaseConnection;
import dao.ProductoDAOImpl;
import java.util.List;
import model.Producto;
import java.sql.Connection;

public class ProductoServiceImpl {

    private final ProductoDAOImpl productoDAO;

    public ProductoServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }

    /**
     * Valida los datos de un producto antes de insertarlo o actualizarlo.
     * Verifica que el nombre no sea vacío, el precio y la cantidad sean mayores
     * a cero, y que la categoría asociada exista si se especifica.
     *
     * @param p Producto a validar.
     * @param conn Conexión activa a la base de datos para validar existencia de
     * categoría.
     * @throws Exception Si alguna validación falla.
     */
    private void validar(Producto p, Connection conn) throws Exception {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        if (p.getPrecio() <= 0) {
            throw new Exception("El precio debe ser mayor a 0.");
        }
        if (p.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0.");
        }
        if (p.getIdCategoria() != 0) {
            if (!productoDAO.existeCategoria(conn, p.getIdCategoria())) {
                throw new Exception("La categoría no existe.");
            }
        }
    }

    /**
     * Crea un nuevo producto en la base de datos tras validar los datos.
     * Realiza la operación en una transacción.
     *
     * @param p Producto a crear.
     * @return El producto creado con su ID asignado.
     * @throws Exception Si ocurre un error en la validación o en la operación
     * de base de datos.
     */
    public Producto crear(Producto p) throws Exception {

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validar(p, conn);
            productoDAO.crear(p, conn);
            conn.commit();
            return p;
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id Identificador del producto.
     * @return Producto encontrado o null si no existe.
     * @throws Exception Si ocurre un error en la operación de base de datos.
     */
    public Producto leer(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return productoDAO.leer(id, conn);
        }
    }

    /**
     * Actualiza un producto existente tras validar sus datos. Realiza la
     * operación en una transacción.
     *
     * @param p Producto con los datos actualizados.
     * @return Producto actualizado.
     * @throws Exception Si ocurre un error en la validación o en la operación
     * de base de datos.
     */
    public Producto actualizar(Producto p) throws Exception {

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validar(p, conn);
            productoDAO.actualizar(p, conn);

            conn.commit();
            return p;
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID. Realiza la operación
     * en una transacción.
     *
     * @param id Identificador del producto a eliminar.
     * @throws Exception Si ocurre un error en la operación de base de datos.
     */
    public void eliminar(int id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            productoDAO.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Lista todos los productos almacenados en la base de datos.
     *
     * @return Lista de productos.
     * @throws Exception Si ocurre un error en la operación de base de datos.
     */
    public List<Producto> listar() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return productoDAO.listar(conn);
        }
    }

    /**
     * Lista los productos filtrados por una categoría específica.
     *
     * @param idCategoria ID de la categoría para filtrar productos.
     * @return Lista de productos que pertenecen a la categoría dada.
     * @throws Exception Si ocurre un error en la operación de base de datos.
     */
    public List<Producto> listarPorCategoria(int idCategoria) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return productoDAO.listarPorCategoria(conn, idCategoria);
        }
    }
}
