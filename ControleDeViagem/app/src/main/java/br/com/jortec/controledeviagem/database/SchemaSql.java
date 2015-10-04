package br.com.jortec.controledeviagem.database;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class SchemaSql {

    public static String getCriarTabelaViagem (){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS viagem( ");
        sql.append("_id          integer not null primary key autoincrement, ");
        sql.append("destino   varchar(40) not null, ");
        sql.append("tipo integer, ");
        sql.append("data_chegada date, ");
        sql.append("data_partida date, ");
        sql.append("orcamento double, ");
        sql.append("quantidade_pessoas integer, ");
        sql.append("imagem varchar(200) ");
        sql.append(");");

        return sql.toString();
    }

    public static String getCriarTabelaGasto (){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS gasto( ");
        sql.append("_id          integer not null primary key autoincrement, ");
        sql.append("categoria    varchar(40) not null, ");
        sql.append("valor double, ");
        sql.append("data date, ");
        sql.append("descricao varchar(40), ");
        sql.append("local varchar(40), ");
        sql.append("viagem_id integer, FOREIGN KEY(viagem_id) REFERENCES viagem(_id) ");
        sql.append(");");

        return sql.toString();
    }

}
