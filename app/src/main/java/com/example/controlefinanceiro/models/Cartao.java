package com.example.controlefinanceiro.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Cartao {

    private String cartaoID;
    private String usuarioID;
    private Double credito;
    private Double creditoGasto;
    private Double debito;

    public Cartao() {

    }

    public Cartao(String cartaoID, String usuarioID, Double credito, Double debito) {
        this.cartaoID = cartaoID;
        this.usuarioID = usuarioID;
        this.credito = credito;
        this.debito = debito;
    }

    public String getCartaoID() {
        return cartaoID;
    }

    public void setCartaoID(String cartaoID) {
        this.cartaoID = cartaoID;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

    public Double getCreditoGasto() {
        return creditoGasto;
    }

    public void setCreditoGasto(Double creditoGasto) {
        this.creditoGasto = creditoGasto;
    }

    public Double getDebito() {
        return debito;
    }

    public void setDebito(Double debito) {
        this.debito = debito;
    }
}
