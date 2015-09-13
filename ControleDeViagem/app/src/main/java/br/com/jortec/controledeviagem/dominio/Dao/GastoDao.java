package br.com.jortec.controledeviagem.dominio.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.jortec.controledeviagem.GastoListaActivity;
import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;
import br.com.jortec.controledeviagem.dominio.util.Formate;

/**
 * Created by Jorliano on 09/09/2015.
 */
public class GastoDao {
    DatabaseSql db;
    SQLiteDatabase conexao;
    Gasto gasto;


    public GastoDao(Context context)
    {
      db = new DatabaseSql(context);
    }

    public SQLiteDatabase getConexao(){
        if(conexao == null){
            conexao = db.getWritableDatabase();
        }

        return  conexao;
    }

    public void fecharConexao(){
        conexao.close();
    }

    public static Double CalculaValorTotalGasto(SQLiteDatabase conexao, int id){

        Cursor cursor = conexao.rawQuery("Select sum(valor) from gasto where viagem_id = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        Double total = cursor.getDouble(0);

        cursor.close();
        return total;
    }

    public ArrayList<HashMap<String,Object>> listarGasto(Context context,int viagem_id){
        Cursor cursor = getConexao().rawQuery("Select * from gasto where viagem_id = ?", new String[]{String.valueOf(viagem_id)});

        cursor.moveToFirst();

        ArrayList<HashMap<String,Object>> listaGasto = new ArrayList<HashMap<String,Object>>();
        int cont = 0;

        while (cursor.moveToNext()){
            HashMap<String,Object> item = new HashMap<String,Object>();

            item.put(Gasto.ID,cursor.getInt(cursor.getColumnIndex(Gasto.ID)));
            item.put(Gasto.CATEGORIA, cursor.getString(cursor.getColumnIndex(Gasto.CATEGORIA)));
            item.put(Gasto.DESCRICAO, cursor.getString(cursor.getColumnIndex(Gasto.DESCRICAO)));
            item.put(Gasto.LOCAL, cursor.getString(cursor.getColumnIndex(Gasto.LOCAL)));
            item.put(Gasto.VALOR, "R$ " + Formate.doubleParaMonetario(cursor.getDouble(cursor.getColumnIndex(Gasto.VALOR))));
            item.put(Gasto.DATA, Formate.dataParaString(new Date(cursor.getLong(cursor.getColumnIndex(Gasto.DATA)))));

            listaGasto.add(item);

        }
        Toast.makeText(context, "Quantidade de objetos salvos "+cont, Toast.LENGTH_LONG).show();


        return  listaGasto;
    }

    public Gasto pesquisarPorId(int id){
        Cursor cursor = getConexao().rawQuery("Select * from gasto where _id = ?",new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        Gasto gastoSelecionado = new Gasto();

        gastoSelecionado.set_id(cursor.getInt(cursor.getColumnIndex(Gasto.ID)));
        gastoSelecionado.setCategoria(cursor.getString(cursor.getColumnIndex(Gasto.CATEGORIA)));
        gastoSelecionado.setDescricao(cursor.getString(cursor.getColumnIndex(Gasto.DESCRICAO)));
        gastoSelecionado.setLocal(cursor.getString(cursor.getColumnIndex(Gasto.LOCAL)));
        gastoSelecionado.setValor(cursor.getDouble(cursor.getColumnIndex(Gasto.VALOR)));
        gastoSelecionado.setData(new Date(cursor.getLong(cursor.getColumnIndex(Gasto.DATA))));
        gastoSelecionado.setViagem_id(cursor.getInt(cursor.getColumnIndex(Gasto.VIAGEM_ID)));

        cursor.close();
        return  gastoSelecionado;
    }

    public void salvarGasto(Context context, Gasto gasto){
        try {
            getConexao().insert(Gasto.TABELA, null, preencherDados(gasto));
            Toast.makeText(context, "Dados salvos com sucesso ", Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            Toast.makeText(context," Erro ao salvar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void editarGasto(Context context, Gasto gasto, int id){
        try {
            getConexao().update(Gasto.TABELA, preencherDados(gasto), "_id = ?", new String[]{String.valueOf(id)});
            Toast.makeText(context, "Dados atualizados com sucesso ", Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(context," Erro ao atualizar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void deletarGasto(Context context,int id){
        try {
            getConexao().delete(Gasto.TABELA, "_id = ?", new String[]{String.valueOf(id)});
            Toast.makeText(context, "Dados foram deletados com sucesso ", Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(context," Erro ao deletar "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public ContentValues preencherDados(Gasto gasto){
        ContentValues contentValues = new ContentValues();

        contentValues.put(gasto.VIAGEM_ID,gasto.getViagem_id());
        contentValues.put(gasto.CATEGORIA,gasto.getCategoria());
        contentValues.put(gasto.DESCRICAO,gasto.getDescricao());
        contentValues.put(gasto.VALOR,gasto.getValor());
        contentValues.put(gasto.LOCAL,gasto.getLocal());
        contentValues.put(gasto.DATA, gasto.getData().getTime());

        return contentValues;

    }


}
