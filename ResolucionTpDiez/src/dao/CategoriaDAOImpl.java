package dao;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;

public class CategoriaDAOImpl implements GenericDAO<Categoria> {

    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param categoria Objeto Categoria que contiene los datos a insertar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    @Override
    public void crear(Categoria categoria, Connection conn) throws Exception {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Lee y devuelve una categoría a partir de su ID.
     *
     * @param id Identificador único de la categoría.
     * @param conn Conexión activa a la base de datos.
     * @return La categoría encontrada o null si no existe.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    @Override
    public Categoria leer(int id, Connection conn) throws Exception {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
                }
            }
        }
        return null;
    }

    /**
     * Obtiene una lista con todas las categorías almacenadas en la base de
     * datos.
     *
     * @param conn Conexión activa a la base de datos.
     * @return Lista de categorías.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    @Override
    public List<Categoria> listar(Connection conn) throws Exception {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
                lista.add(c);
            }
        }

        return lista;
    }

    /**
     * Actualiza los datos de una categoría existente en la base de datos.
     *
     * @param categoria Objeto Categoria con los datos actualizados (debe
     * incluir el id).
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    @Override
    public void actualizar(Categoria categoria, Connection conn) throws Exception {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina una categoría de la base de datos por su ID.
     *
     * @param id Identificador único de la categoría a eliminar.
     * @param conn Conexión activa a la base de datos.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    @Override
    public void eliminar(int id, Connection conn) throws Exception {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Verifica si ya existe una categoría con el nombre especificado.
     *
     * @param nombre Nombre de la categoría a verificar.
     * @param conn Conexión activa a la base de datos.
     * @return true si existe una categoría con ese nombre, false en caso
     * contrario.
     * @throws Exception Si ocurre algún error durante la ejecución SQL.
     */
    public boolean existeNombre(String nombre, Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
