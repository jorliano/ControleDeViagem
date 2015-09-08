package br.com.jortec.controledeviagem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class DatabaseSql extends SQLiteOpenHelper {

    public static final String BANCO_DADOS = "ControleViagens";
    public static int VERSAO = 1;

    public DatabaseSql (Context context){
        super(context,BANCO_DADOS,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SchemaSql.getCriarTabelaViagem());
        db.execSQL(SchemaSql.getCriarTabelaGasto());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
