package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.Dao.ViagemDao;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;
import br.com.jortec.controledeviagem.dominio.util.Formate;

public class CadastroViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnDataPartida;
    private Button btnDataChegada;
    private Button btnSalvar;
    private EditText destino;
    private EditText orcamento;
    private EditText quantidadePessoas;
    private RadioGroup tipoSelecionado;
    private int tipo;
    private int id = 0;


    ViagemDao dao;
    Viagem viagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagem);

        dao = new ViagemDao(this);

       //Associar valores do meu layout de viagens
        btnDataChegada = (Button) findViewById(R.id.btnDataChegada);
        btnDataPartida = (Button) findViewById(R.id.btnDataPartida);
        btnSalvar = (Button) findViewById(R.id.btnSalvarViagem);
        destino = (EditText) findViewById(R.id.edtDestino);
        orcamento = (EditText) findViewById(R.id.edtOrcamento);
        quantidadePessoas = (EditText) findViewById(R.id.edtPessoas);
        tipoSelecionado =(RadioGroup) findViewById(R.id.radioGoup);

        tipo = tipoSelecionado.getCheckedRadioButtonId();

        //Eventos dos componentes
        btnDataChegada.setOnClickListener(this);
        btnDataPartida.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);

        //pega o id da viagem selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(MinhasViagensActivity.VIAGEM_ID)){
            id = bundle.getInt(MinhasViagensActivity.VIAGEM_ID);
            carregarViagemSelecionada();

        }else{
            viagem = new Viagem();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_viagem, menu);
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
        switch (v.getId()){
            case R.id.btnDataChegada:
                exibirData(btnDataChegada);
                break;
            case R.id.btnDataPartida:
                exibirData(btnDataPartida);
                break;
            case R.id.btnSalvarViagem:
                if(id == 0) {
                    preencerDadosViagem();
                    dao.salvarViagem(this,viagem);
                    startActivity(new Intent(this,MinhasViagensActivity.class));
                }else{
                    preencerDadosViagem();
                    dao.editarViagem(this, viagem, id);
                    startActivity(new Intent(this, MinhasViagensActivity.class));
                }
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


    public void preencerDadosViagem()  {

        viagem.setDestino(destino.getText().toString());
        viagem.setOrcamento(Formate.MonetarioParaDouble(orcamento.getText().toString()));
        viagem.setQuantidade_pessoas(Integer.parseInt(quantidadePessoas.getText().toString()));
        viagem.setData_partida(Formate.stringParaData(btnDataPartida.getText().toString()));
        viagem.setData_chegada(Formate.stringParaData(btnDataChegada.getText().toString()));
        if(tipo == R.id.rbtnLazer)
            viagem.setTipo(1);
        else
            viagem.setTipo(2);

    }
    public void carregarViagemSelecionada()  {

        viagem = dao.pesquisarPorId(id);

        if(viagem != null){

            btnDataChegada.setText(Formate.dataParaString(viagem.getData_chegada()));
            btnDataPartida.setText(Formate.dataParaString(viagem.getData_partida()));
            destino.setText(viagem.getDestino());
            orcamento.setText(Formate.doubleParaMonetario(viagem.getOrcamento()));
            quantidadePessoas.setText(String.valueOf(viagem.getQuantidade_pessoas()));

            tipo = viagem.getTipo();
            if(tipo == 0){
                tipoSelecionado.check(R.id.rbtnLazer);
            }else{
                tipoSelecionado.check(R.id.rbtnNegocio);
            }

        }


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
