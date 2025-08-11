package service;

import dao.CategoriaDAOImpl;
import model.Categoria;
import java.sql.Connection;
import config.DatabaseConnection;

import java.util.List;

public class CategoriaServiceImpl {

    private final CategoriaDAOImpl categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAOImpl categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Crea una nueva categoría en la base de datos. Valida que el nombre no sea
     * nulo ni vacío y que no exista ya una categoría con el mismo nombre.
     *
     * @param categoria Objeto Categoría a crear.
     * @throws IllegalArgumentException Si el nombre es nulo, vacío o ya existe
     * una categoría con ese nombre.
     * @throws Exception Si ocurre algún error durante la operación en la base
     * de datos.
     */
    public void crear(Categoria categoria) throws Exception {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (categoriaDAO.existeNombre(categoria.getNombre(), conn)) {
                throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
            }

            categoriaDAO.crear(categoria, conn);
            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Actualiza una categoría existente en la base de datos.
     *
     * @param categoria Objeto Categoría con datos actualizados (debe incluir el
     * id).
     * @throws Exception Si ocurre algún error durante la operación en la base
     * de datos.
     */
    public void actualizar(Categoria categoria) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            categoriaDAO.actualizar(categoria, conn);

            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Elimina una categoría de la base de datos según su ID.
     *
     * @param id Identificador de la categoría a eliminar.
     * @throws Exception Si ocurre algún error durante la operación en la base
     * de datos.
     */
    public void eliminar(int id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            categoriaDAO.eliminar(id, conn);

            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id Identificador de la categoría.
     * @return Objeto Categoría o null si no existe.
     * @throws Exception Si ocurre algún error durante la operación en la base
     * de datos.
     */
    public Categoria leer(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return categoriaDAO.leer(id, conn);
        }
    }

    /**
     * Lista todas las categorías existentes en la base de datos.
     *
     * @return Lista de categorías.
     * @throws Exception Si ocurre algún error durante la operación en la base
     * de datos.
     */
    public List<Categoria> listar() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return categoriaDAO.listar(conn);
        }
    }

}
