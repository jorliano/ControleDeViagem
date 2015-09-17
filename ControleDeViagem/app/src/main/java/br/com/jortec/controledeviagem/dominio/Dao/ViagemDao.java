package br.com.jortec.controledeviagem.dominio.Dao;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;
import br.com.jortec.controledeviagem.dominio.util.Formate;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class ViagemDao {


    SQLiteDatabase conexao;
    DatabaseSql db;
    Viagem viagem;
    GastoDao gastoDao;

    public ViagemDao(Context context){

        db = new DatabaseSql(context);
    }

    public SQLiteDatabase getConexao (){
        if (conexao ==  null){
            conexao = db.getWritableDatabase();
        }

        return conexao;
    }

    public ArrayList<HashMap<String, Object>> listarViagem(Context context,Double valorLimite) {

        ArrayList<HashMap<String, Object>> viagemLista = new ArrayList<HashMap<String, Object>>();


        Cursor cursor = getConexao().query(viagem.TABELA, null, null, null, null, null, null);

       // cursor.moveToFirst();

        while (cursor.moveToNext()){

            HashMap<String,Object> item = new HashMap<String, Object>();

            item.put(viagem.ID,cursor.getInt(cursor.getColumnIndex(viagem.ID)));
            item.put(viagem.DESTINO,cursor.getString(cursor.getColumnIndex(viagem.DESTINO)));
            item.put(viagem.ORCAMENTO,cursor.getDouble(cursor.getColumnIndex(viagem.ORCAMENTO)));
            item.put(viagem.TIPO,cursor.getInt(cursor.getColumnIndex(viagem.TIPO)));
            item.put(viagem.DATA_PARTIDA, new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_PARTIDA))));
            item.put(viagem.DATA_CHEGADA, new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_CHEGADA))));
            item.put(viagem.QUANTIDADE_PESSOAS, cursor.getInt(cursor.getColumnIndex(viagem.QUANTIDADE_PESSOAS)));


            String data = Formate.dataParaString(new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_PARTIDA))))+" a "+
                          Formate.dataParaString(new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_CHEGADA))));

            item.put("data", data);

            Double totalGasto = gastoDao.CalculaValorTotalGasto(getConexao(),cursor.getInt(cursor.getColumnIndex(viagem.ID)));

            item.put("totalGasto","Total Gasto R$ "+ Formate.doubleParaMonetario(totalGasto));

            Double orcamento = cursor.getDouble(cursor.getColumnIndex(viagem.ORCAMENTO));
            Double alerta = orcamento * valorLimite /100;

            Double[] valores = new Double[]{orcamento,alerta,totalGasto};

            item.put("barraProgresso",valores);

            viagemLista.add(item);

        }

       return  viagemLista;


    }
    public Viagem pesquisarPorId(int id){
        Viagem viagem = new Viagem();

        Cursor cursor = getConexao().rawQuery("Select * from viagem where _id = ?",new String[]{String.valueOf(id)});


        viagem.setId(cursor.getInt(cursor.getColumnIndex(viagem.ID)));
        viagem.setDestino(cursor.getString(cursor.getColumnIndex(viagem.DESTINO)));
        viagem.setOrcamento(cursor.getDouble(cursor.getColumnIndex(viagem.ORCAMENTO)));
        viagem.setTipo(cursor.getInt(cursor.getColumnIndex(viagem.TIPO)));
        viagem.setData_partida(new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_PARTIDA))));
        viagem.setData_chegada(new Date(cursor.getLong(cursor.getColumnIndex(viagem.DATA_CHEGADA))));
        viagem.setQuantidade_pessoas(cursor.getInt(cursor.getColumnIndex(viagem.QUANTIDADE_PESSOAS)));


        return viagem;
    }

    public void salvarViagem(Context context ,Viagem viagem){
        try {
            getConexao().insert(Viagem.TABELA, null, preencherDados(viagem));
            Toast.makeText(context, "Dados salvo com sucesso ", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(context,"Erro ao salvar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void editarViagem(Context context ,Viagem viagem, int id){
        try {
            getConexao().update(Viagem.TABELA, preencherDados(viagem), "_id =?", new String[]{String.valueOf(viagem.getId())});
            Toast.makeText(context, "Dados atualizado com sucesso ", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(context,"Erro ao alterar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void deletarViagem(Context context ,int id){
        try {
            String where[] = {String.valueOf(id)};

            getConexao().delete(Gasto.TABELA,"viagem_id = ?", where);
            getConexao().delete(Viagem.TABELA, "_id =?", where);
            Toast.makeText(context, "Os dados foram removidos  ", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(context,"Erro ao deletar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public ContentValues preencherDados(Viagem viagem){
        ContentValues values = new ContentValues();

        values.put(Viagem.DESTINO, viagem.getDestino());
        values.put(Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(Viagem.TIPO, viagem.getTipo());
        values.put(Viagem.DATA_CHEGADA, viagem.getData_chegada().getTime());
        values.put(Viagem.DATA_PARTIDA, viagem.getData_partida().getTime());
        values.put(Viagem.QUANTIDADE_PESSOAS, viagem.getQuantidade_pessoas());

        return values;
    }


}
