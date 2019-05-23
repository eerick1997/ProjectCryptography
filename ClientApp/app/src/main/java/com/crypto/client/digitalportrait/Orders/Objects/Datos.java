package com.crypto.client.digitalportrait.Orders.Objects;

import com.google.firebase.firestore.Exclude;

public class Datos {
    private String documentId;
    private String descripcion;
    private String fecha;
    private String imagen;
    private String email;
    private String estado;
    private String publicKeyArtist;
    private String signatureArtist;
    private String imageArtist;
    private String publicKeyClient;
    private String signatureClient;

    public Datos(){

    }
    @Exclude
    public String getDocumentId(){
        return documentId;
    }
    public void setDocumentId(String documentId){
        this.documentId=documentId;
    }

    public Datos(String descripcion, String fecha, String imagen, String email, String estado,
                 String publicKeyArtist, String signatureArtist, String imageArtist,
                 String publicKeyClient, String signatureClient) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.imagen = imagen;
        this.email = email;
        this.estado = estado;
        this.publicKeyArtist = publicKeyArtist;
        this.signatureArtist = signatureArtist;
        this.imageArtist = imageArtist;
        this.publicKeyClient = publicKeyClient;
        this.signatureClient = signatureClient;
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

    public String getPublicKeyArtist() {
        return publicKeyArtist;
    }

    public void setPublicKeyArtist(String publicKeyArtist) {
        this.publicKeyArtist = publicKeyArtist;
    }

    public String getSignatureArtist() {
        return signatureArtist;
    }

    public void setSignatureArtist(String signatureArtist) {
        this.signatureArtist = signatureArtist;
    }

    public String getImageArtist() {
        return imageArtist;
    }

    public void setImageArtist(String imageArtist) {
        this.imageArtist = imageArtist;
    }

    public String getPublicKeyClient() {
        return publicKeyClient;
    }

    public void setPublicKeyClient(String publicKeyClient) {
        this.publicKeyClient = publicKeyClient;
    }

    public String getSignatureClient() {
        return signatureClient;
    }

    public void setSignatureClient(String signatureClient) {
        this.signatureClient = signatureClient;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
