package br.com.jortec.controledeviagem.dominio.Dao;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class ViagemDao {


    SQLiteDatabase conexao;
    Viagem viagem;
    GastoDao gastoDao;

    public ViagemDao(SQLiteDatabase conexao){
        this.conexao = conexao;
        viagem = new Viagem();

        gastoDao = new GastoDao(conexao);

    }

    public SimpleAdapter listarViagem(Context context, int lyout, String[] de,int[] para){

        ArrayList<HashMap<String,Object>> viagemLista = new ArrayList<HashMap<String,Object>>();

        SimpleAdapter adp = new SimpleAdapter(context,viagemLista,lyout,de,para);



        Cursor cursor = conexao.query(viagem.TABELA,null,null,null,null,null,null);

        cursor.moveToFirst();

        while (cursor.moveToNext()){

            HashMap<String,Object> item = new HashMap<String, Object>();

            item.put(viagem.ID,cursor.getInt(cursor.getColumnIndex(viagem.ID)));
            item.put(viagem.DESTINO,cursor.getString(cursor.getColumnIndex(viagem.DESTINO)));
            item.put(viagem.ORCAMENTO,cursor.getDouble(cursor.getColumnIndex(viagem.ORCAMENTO)));
            item.put(viagem.TIPO,cursor.getInt(cursor.getColumnIndex(viagem.TIPO)));
            item.put(viagem.DATA_PARTIDA, new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_PARTIDA))));
            item.put(viagem.DATA_CHEGADA, new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_CHEGADA))));
            item.put(viagem.QUANTIDADE_PESSOAS, cursor.getInt(cursor.getColumnIndex(viagem.QUANTIDADE_PESSOAS)));





            String data = formatData( new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_PARTIDA))))+" a "+
                         formatData( new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_CHEGADA))));

            item.put("data",data);

          /*  Double totalGasto = gastoDao.CalculaValorTotalGasto(cursor.getString(cursor.getColumnIndex(viagem.ID)));

            item.put("total","Total Gasro R$ "+totalGasto);

            Double[] valores = new Double[]{cursor.getDouble(cursor.getColumnIndex(viagem.ORCAMENTO)),
                    300.0,totalGasto};

            item.put("barra",valores);*/

            viagemLista.add(item);

        }

       return  adp;


    }
    public void salvarViagem(Viagem viagem){
      conexao.insertOrThrow(Viagem.TABELA,null,preencherDados(viagem));

    }
    public void editarViagem(Viagem viagem){
      conexao.update(Viagem.TABELA, preencherDados(viagem), "_id =?", new String[]{String.valueOf(viagem.getId())});
    }
    public void deletarViagem(long id){
     conexao.delete(Viagem.TABELA, "_id =?", new String[]{String.valueOf(id)});
    }

    public ContentValues preencherDados(Viagem viagem){
        ContentValues values = new ContentValues();

        values.put(Viagem.DESTINO, viagem.getDestino());
        values.put(Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(Viagem.TIPO, viagem.getTipo());
        values.put(Viagem.DATA_CHEGADA, new Date().getTime());
        values.put(Viagem.DATA_PARTIDA,  new Date().getTime());
        values.put(Viagem.DATA_CHEGADA, viagem.getData_chegada().getTime());
        values.put(Viagem.DATA_PARTIDA, viagem.getData_partida().getTime());
        values.put(Viagem.QUANTIDADE_PESSOAS, viagem.getQuantidade_pessoas());

        return values;
    }

    public String formatData(Date data){
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataFormatada = format.format(data);

        return dataFormatada;
    }

}
