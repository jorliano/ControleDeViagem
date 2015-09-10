package br.com.jortec.controledeviagem.dominio.entidade;

import java.util.Date;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class Viagem {

    public static String TABELA ="viagem";

    public static String ID ="_id";
    public static String DESTINO ="destino";
    public static String TIPO ="tipo";
    public static String ORCAMENTO ="orcamento";
    public static String QUANTIDADE_PESSOAS ="quantidade_pessoas";
    public static String DATA_CHEGADA ="data_chegada";
    public static String DATA_PARTIDA ="data_partida";




    private int _id;
    private String destino;
    private int tipo;
    private double orcamento;
    private int quantidade_pessoas;
    private Date data_chegada;
    private Date data_partida;


    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public int getQuantidade_pessoas() {
        return quantidade_pessoas;
    }

    public void setQuantidade_pessoas(int quantidade_pessoas) {
        this.quantidade_pessoas = quantidade_pessoas;
    }

    public Date getData_chegada() {
        return data_chegada;
    }

    public void setData_chegada(Date data_chegada) {
        this.data_chegada = data_chegada;
    }


    public Date getData_partida() {
        return data_partida;
    }

    public void setData_partida(Date data_partida) {
        this.data_partida = data_partida;
    }
}
