package br.com.jortec.controledeviagem;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.Dao.GastoDao;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;

public class GastoViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spnTipoGasto;
    private EditText valor;
    private EditText descricao;
    private EditText local;
    private Button data;
    private Button btnSalvar;

    SQLiteDatabase conexao;
    DatabaseSql db;
    GastoDao dao;
    Gasto gasto;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_viagem);

        spnTipoGasto = (Spinner) findViewById(R.id.spnTipoGasto);
        valor = (EditText) findViewById(R.id.edtValor);
        descricao  = (EditText) findViewById(R.id.edtDescricao);
        local  = (EditText) findViewById(R.id.edtLocal);
        data = (Button) findViewById(R.id.btnDataGasto);
        btnSalvar = (Button) findViewById(R.id.btnSalvarGasto);



        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.categoria_gasto,
                android.R.layout.simple_spinner_dropdown_item);

        spnTipoGasto.setAdapter(arrayAdapter);


        btnSalvar.setOnClickListener(this);


        try {
            db = new DatabaseSql(this);
            conexao = db.getWritableDatabase();

            dao = new GastoDao(conexao);

        }catch (Exception ex){

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gasto_viagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        dao.salvarGasto(conexao,gasto);
    }

    public Gasto preencherDados(int id){
        Gasto gasto = new Gasto();

        gasto.setCategoria(String.valueOf(spnTipoGasto.getSelectedItemPosition()));
        gasto.setDescricao(descricao.getText().toString());
        gasto.setValor(Double.parseDouble(valor.getText().toString()));
        gasto.setLocal(local.getText().toString());
        gasto.setData(new Date());
        gasto.setViagem_id(id);


        return gasto;
    }
}
