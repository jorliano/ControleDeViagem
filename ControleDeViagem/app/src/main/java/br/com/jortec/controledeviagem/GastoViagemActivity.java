package br.com.jortec.controledeviagem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.Dao.GastoDao;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;
import br.com.jortec.controledeviagem.dominio.util.Formate;

public class GastoViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Spinner spnTipoGasto;
    private EditText valor;
    private EditText descricao;
    private EditText local;
    private Button data;
    private Button btnSalvar;

    GastoDao dao;
    Gasto gasto;
    int viagem_id;
    int id_gasto;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_viagem);

        dao = new GastoDao(this);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.gasto_toolbar);
        toolbar.setTitle("Gastos");
        toolbar.setSubtitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        data.setOnClickListener(this);


        //Pegar conteudo  de outras acticitys
        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.containsKey(MinhasViagensActivity.VIAGEM_ID)){
            viagem_id = bundle.getInt(MinhasViagensActivity.VIAGEM_ID);

        }

        if(bundle != null && bundle.containsKey(GastoListaActivity.ID_GASTO)){
              toolbar.setSubtitle("Editar");
              id_gasto = bundle.getInt(GastoListaActivity.ID_GASTO);
              carregarGastoSelecionado();
        }
        else{
                gasto = new Gasto();
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
        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSalvarGasto:
                if (id_gasto == 0){
                    preencherDados(viagem_id);
                    dao.salvarGasto(this, gasto);
                }else {
                    preencherDados(gasto.getViagem_id());
                    dao.editarGasto(this,gasto,id_gasto);
                }

                startActivity(new Intent(this, MinhasViagensActivity.class));
                break;
            case R.id.btnDataGasto:
                exibirData(data);
                break;
        }


    }

    public void exibirData(Button btnData){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,new SeleciionaDataPicker(btnData),ano,mes,dia);
        dpd.show();
    }




    public void carregarGastoSelecionado(){
        gasto = dao.pesquisarPorId(id_gasto);

        spnTipoGasto.setSelection(Integer.parseInt(gasto.getCategoria()));
        valor.setText(Formate.doubleParaMonetario(gasto.getValor()));
        descricao.setText(gasto.getDescricao());
        local.setText(gasto.getLocal());
        data.setText(Formate.dataParaString(gasto.getData()));

    }

    public Gasto preencherDados(int id){

        gasto.setCategoria(String.valueOf(spnTipoGasto.getSelectedItemPosition()));
        gasto.setDescricao(descricao.getText().toString());
        gasto.setValor(Formate.MonetarioParaDouble(valor.getText().toString()));
        gasto.setLocal(local.getText().toString());
        gasto.setData(new Date());
        gasto.setViagem_id(id);

        return gasto;
    }

    private class  SeleciionaDataPicker implements DatePickerDialog.OnDateSetListener{
        Button btnData;

        public SeleciionaDataPicker(Button btnData){
            this.btnData = btnData;
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            Date dataSelecionada = calendar.getTime();

            String data = Formate.dataParaString(dataSelecionada);

            btnData.setText(data);
        }
    }


}
