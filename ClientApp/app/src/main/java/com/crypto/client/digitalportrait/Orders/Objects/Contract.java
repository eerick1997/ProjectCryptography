package com.crypto.client.digitalportrait.Orders.Objects;

import com.google.firebase.firestore.Exclude;

public class Contract {

    private byte[] publicKey;
    private String email;
    private String date;
    private String documentId;

    public Contract(){}

    public Contract(byte[] publicKey, String email, String date) {
        this.publicKey = publicKey;
        this.email = email;
        this.date = date;
    }

    @Exclude
    public String getDocumentId(){
        return documentId;
    }

    public void setDocumentId(String documentId){
        this.documentId = documentId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
