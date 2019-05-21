package com.crypto.client.digitalportrait.Objetos;

public class Pedido{

    //Constantes
    private static final String TAG = "Paciente.java";
    //Variables
    private String descripcion;
    private String fecha;
    private String imagen;

    public Pedido(String descripcion,String fecha,String imagen){
        setDescripcion(descripcion);
        setFecha(fecha);
        setImagen(imagen);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
