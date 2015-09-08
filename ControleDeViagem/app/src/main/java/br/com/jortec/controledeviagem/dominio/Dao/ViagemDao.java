package br.com.jortec.controledeviagem.dominio.Dao;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class ViagemDao {


    SQLiteDatabase conexao;
    Viagem viagem;

    public ViagemDao(SQLiteDatabase conexao){
        this.conexao = conexao;
        viagem = new Viagem();
    }

    public void listarViagem(Context context){

    }
    public void salvarViagem(Viagem viagem){
      conexao.insertOrThrow(Viagem.TABELA,null,preencherDados(viagem));

    }
    public void editarViagem(Viagem viagem){
      conexao.update(Viagem.TABELA,preencherDados(viagem),"_id =?",new String[]{String.valueOf(viagem.getId())});
    }
    public void deletarViagem(long id){
     conexao.delete(Viagem.TABELA,"_id =?",new String[]{String.valueOf(id)});
    }

    public ContentValues preencherDados(Viagem viagem){
        ContentValues values = new ContentValues();

        values.put(Viagem.DESTINO, viagem.getDestino());
        values.put(Viagem.ORCAMENTO, viagem.getOrcamento());
        values.put(Viagem.TIPO, viagem.getTipo());
        values.put(Viagem.DATA_CHEGADA, new Date().getTime());
        values.put(Viagem.DATA_PARTIDA,  new Date().getTime());
       // values.put(Viagem.DATA_CHEGADA, viagem.getData_chegada().getTime());
       // values.put(Viagem.DATA_PARTIDA, viagem.getData_partida().getTime());
        values.put(Viagem.QUANTIDADE_PESSOAS, viagem.getQuantidade_pessoas());

        return values;
    }

}
