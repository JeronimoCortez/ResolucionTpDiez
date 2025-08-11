package dao;

import java.sql.*;
import java.util.*;
import model.Producto;

public class ProductoDAOImpl implements GenericDAO<Producto> {

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param producto Objeto Producto con los datos a insertar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    @Override
    public void crear(Producto producto, Connection conn) throws Exception {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, cantidad, id_categoria) values(?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.setInt(5, producto.getIdCategoria());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Obtiene un producto de la base de datos por su ID.
     *
     * @param id Identificador único del producto.
     * @param conn Conexión activa a la base de datos.
     * @return Producto encontrado o null si no existe.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    @Override
    public Producto leer(int id, Connection conn) throws Exception {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer idCat = rs.getObject("id_categoria") != null ? rs.getInt("id_categoria") : null;
                return new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        idCat
                );
            }
        }
        return null;
    }

    /**
     * Lista todos los productos existentes en la base de datos.
     *
     * @param conn Conexión activa a la base de datos.
     * @return Lista de productos.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    @Override
    public List<Producto> listar(Connection conn) throws Exception {
        String sql = "SELECT * FROM productos";
        List<Producto> productos = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Integer idCat = rs.getObject("id_categoria") != null ? rs.getInt("id_categoria") : null;
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        idCat
                ));
            }
        }
        return productos;
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto Producto con los datos actualizados (debe incluir el id).
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    @Override
    public void actualizar(Producto producto, Connection conn) throws Exception {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, cantidad = ?, id_categoria = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.setInt(5, producto.getIdCategoria());
            stmt.setInt(6, producto.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param id Identificador del producto a eliminar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    @Override
    public void eliminar(int id, Connection conn) throws Exception {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Obtiene una lista de productos filtrados por categoría.
     *
     * @param conn Conexión activa a la base de datos.
     * @param idCategoria ID de la categoría para filtrar productos.
     * @return Lista de productos que pertenecen a la categoría dada.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    public List<Producto> listarPorCategoria(Connection conn, int idCategoria) throws Exception {
        String sql = "SELECT * FROM productos WHERE id_categoria = ?";
        List<Producto> productos = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("cantidad"),
                            rs.getInt("id_categoria")
                    ));
                }
            }
        }
        return productos;
    }

    /**
     * Verifica si existe una categoría con el ID especificado.
     *
     * @param conn Conexión activa a la base de datos.
     * @param idCategoria ID de la categoría a verificar.
     * @return true si la categoría existe, false en caso contrario.
     * @throws Exception Si ocurre un error en la ejecución SQL.
     */
    public boolean existeCategoria(Connection conn, int idCategoria) throws Exception {
        String sql = "SELECT COUNT(*) FROM categorias WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
