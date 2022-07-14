package com.example.controlefinanceiro.models;

import com.google.firebase.database.Exclude;

public class Metas {

    private String usuarioID;
    private String MetaDescricao;
    private Double ValorAcumulado;
    private Double ValorMeta;
    private long TimestampMeta;

    public Metas() {

    }

    public Metas(String usuarioId, String metaDescricao, Double valorAcumulado, Double valorMeta, long timestampMeta) {
        usuarioID = usuarioId;
        MetaDescricao = metaDescricao;
        ValorAcumulado = valorAcumulado;
        ValorMeta = valorMeta;
        TimestampMeta = timestampMeta;

    }

    @Exclude
    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getMetaDescricao() {
        return MetaDescricao;
    }

    public void setMetaDescricao(String metaDescricao) {
        MetaDescricao = metaDescricao;
    }

    public Double getValorAcumulado() {
        return ValorAcumulado;
    }

    public void setValorAcumulado(Double valorAcumulado) {
        ValorAcumulado = valorAcumulado;
    }

    public Double getValorMeta() {
        return ValorMeta;
    }

    public void setValorMeta(Double valorMeta) {
        ValorMeta = valorMeta;
    }

    public long getTimestampMeta() {
        return TimestampMeta;
    }

    public void setTimestampMeta(long timestampMeta) {
        TimestampMeta = timestampMeta;
    }
}
