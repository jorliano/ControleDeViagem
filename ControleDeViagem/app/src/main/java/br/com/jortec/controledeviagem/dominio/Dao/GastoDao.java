package br.com.jortec.controledeviagem.dominio.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.jortec.controledeviagem.dominio.entidade.Gasto;

/**
 * Created by Jorliano on 09/09/2015.
 */
public class GastoDao {
    SQLiteDatabase conexao;
    Gasto gasto;


    public GastoDao(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public Double CalculaValorTotalGasto(String id){

        Cursor cursor = conexao.rawQuery("Select sum(valor) from gasto where _id = ?",new String[]{id});
        cursor.moveToFirst();

        Double total = cursor.getDouble(0);

        cursor.close();
        return total;
    }

    public ArrayList<HashMap<String,Object>> listarGasto(SQLiteDatabase conexao, int viagem_id){
        Cursor cursor = conexao.rawQuery("Select local from gasto where viagem_id = ?",new String[]{String.valueOf(viagem_id)});

        cursor.moveToFirst();

        ArrayList<HashMap<String,Object>> listaGasto = new ArrayList<HashMap<String,Object>>();

        while (cursor.moveToNext()){
            HashMap<String,Object> item = new HashMap<String,Object>();

            item.put(Gasto.CATEGORIA,cursor.getString(cursor.getColumnIndex(Gasto.CATEGORIA)));
            item.put(Gasto.DESCRICAO,cursor.getString(cursor.getColumnIndex(Gasto.DESCRICAO)));
            item.put(Gasto.LOCAL,cursor.getString(cursor.getColumnIndex(Gasto.LOCAL)));
            item.put(Gasto.VALOR,cursor.getDouble(cursor.getColumnIndex(Gasto.VALOR)));

            listaGasto.add(item);
        }

        return  listaGasto;
    }

    public void salvarGasto(SQLiteDatabase conexao, Gasto gasto){
        conexao.insertOrThrow(Gasto.TABELA, null, preencherDados(gasto));
    }

    public void editarGasto(SQLiteDatabase conexao, Gasto gasto, int id){
        conexao.update(Gasto.TABELA,preencherDados(gasto),"_id = ?",new String[]{String.valueOf(id)});
    }

    public void deletarGasto(SQLiteDatabase conexao,int id){
        conexao.delete (Gasto.TABELA,"_id = ?",new String[]{String.valueOf(id)});
    }

    public ContentValues preencherDados(Gasto gasto){
        ContentValues contentValues = new ContentValues();

        contentValues.put(gasto.VIAGEM_ID,gasto.getViagem_id());
        contentValues.put(gasto.CATEGORIA,gasto.getCategoria());
        contentValues.put(gasto.DESCRICAO,gasto.getDescricao());
        contentValues.put(gasto.VALOR,gasto.getValor());
        contentValues.put(gasto.LOCAL,gasto.getLocal());
        contentValues.put(gasto.DATA,gasto.getData().getTime());

        return contentValues;

    }


}
