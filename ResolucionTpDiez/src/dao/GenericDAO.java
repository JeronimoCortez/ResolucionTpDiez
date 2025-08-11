package dao;

import java.util.List;
import java.sql.Connection;

public interface GenericDAO<T> { //Lo declaramos como interface
    
    // Declaramos los metodos abstractos
    void crear(T entity, Connection conn) throws Exception;

    T leer(int id, Connection conn) throws Exception;

    List<T> listar(Connection conn) throws Exception;

    void actualizar(T entity, Connection conn) throws Exception;

    void eliminar(int id, Connection conn) throws Exception;
}
