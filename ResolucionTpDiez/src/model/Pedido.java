package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    //Declaramos los atributos
    private int id;
    private Date fecha;
    private double total;

    // Creamos el constrcutor
    public Pedido(int id, Date fecha, double total) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
    }
    
    public Pedido() {

    }

    // Creamos los getter y setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
