package com.example.controlefinanceiro.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Carteira {

    private String usuarioID;
    private Double saldo;
    private String numCartao;

    public Carteira(){

    }

    public Carteira(String usuarioID, Double saldo, String cartão) {
        this.usuarioID = usuarioID;
        this.saldo = saldo;
        this.numCartao = cartão;
    }

    @Exclude
    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(String numCartao) {
        this.numCartao = numCartao;
    }
}
