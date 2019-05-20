package com.crypto.artist.digitalportrait.Orders.Objects;

import com.google.firebase.firestore.Exclude;

public class Datos {
    private String documentId;
    private String descripcion;
    private String fecha;
    private String imagen;
    private String email;
    public Datos(){

    }
    @Exclude
    public String getDocumentId(){
        return documentId;
    }
    public void setDocumentId(String documentId){
        this.documentId=documentId;
    }
    public Datos(String descripcion,String fecha,String imagen,String email){
        this.descripcion=descripcion;
        this.fecha=fecha;
        this.imagen=imagen;
        this.email=email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
