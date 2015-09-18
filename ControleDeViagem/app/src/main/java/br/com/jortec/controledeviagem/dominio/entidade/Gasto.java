package br.com.jortec.controledeviagem.dominio.entidade;

import java.util.Date;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class Gasto {

    public static String TABELA = "gasto";

    public static String ID = "_id";
    public static String CATEGORIA = "categoria";
    public static String VALOR = "valor";
    public static String DATA = "data";
    public static String DESCRICAO = "descricao";
    public static String LOCAL = "local";
    public static String VIAGEM_ID ="viagem_id";



    private int _id;
    private int viagem_id;
    private String categoria;
    private double valor;
    private Date data;
    private String descricao;
    private String local;


    public int getViagem_id() {
        return viagem_id;
    }

    public void setViagem_id(int viagem_id) {
        this.viagem_id = viagem_id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
