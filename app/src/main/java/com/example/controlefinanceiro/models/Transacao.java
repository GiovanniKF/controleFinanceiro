package com.example.controlefinanceiro.models;

import com.google.firebase.database.Exclude;

public class Transacao {

    private String usuarioID;
    private String tipo;
    private String descricao;
    private String pagamento;
    private String cartaoPagamento;
    private Double valor;
    private long timestamp;

    public Transacao() {
    }

    public Transacao(String usuarioID, String tipo, String descricao, String pagamento, String cartaoPagamento, Double valor, long timestamp) {
        this.usuarioID = usuarioID;
        this.tipo = tipo;
        this.descricao = descricao;
        this.pagamento = pagamento;
        this.cartaoPagamento = cartaoPagamento;
        this.valor = valor;
        this.timestamp = timestamp;
    }

    @Exclude
    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getCartaoPagamento() {
        return cartaoPagamento;
    }

    public void setCartaoPagamento(String cartaoPagamento) {
        this.cartaoPagamento = cartaoPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
