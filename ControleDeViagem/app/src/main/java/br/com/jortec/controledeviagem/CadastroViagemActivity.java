package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.Dao.ViagemDao;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;

public class CadastroViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnDataPartida;
    private Button btnDataChegada;
    private Button btnSalvar;
    private EditText destino;
    private EditText orcamento;
    private EditText quantidadePessoas;
    private RadioGroup tipoSelecionado;
    private int tipo;

    Viagem viagem;
    SQLiteDatabase conexao;
    DatabaseSql db;
    ViagemDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagem);

        btnDataChegada = (Button) findViewById(R.id.btnDataChegada);
        btnDataPartida = (Button) findViewById(R.id.btnDataPartida);
        btnSalvar = (Button) findViewById(R.id.btnSalvarViagem);
        destino = (EditText) findViewById(R.id.edtDestino);
        orcamento = (EditText) findViewById(R.id.edtOrcamento);
        quantidadePessoas = (EditText) findViewById(R.id.edtPessoas);
        tipoSelecionado =(RadioGroup) findViewById(R.id.radioGoup);

        tipo = tipoSelecionado.getCheckedRadioButtonId();


        btnDataChegada.setOnClickListener(this);
        btnDataPartida.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);



        try {
            db = new DatabaseSql(this);
            conexao = db.getWritableDatabase();

            dao = new ViagemDao(conexao);
        }catch (Exception ex){
            Toast.makeText(this,"Erro ao se conectar com o banco "+ex.getMessage(),Toast.LENGTH_LONG).show();
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

        if(v == btnDataChegada)
            exibirData(btnDataChegada);
        else if(v == btnDataPartida)
            exibirData(btnDataPartida);
        else if(v == btnSalvar){
            try{
                preencerDadosViagem();
                dao.salvarViagem(viagem);
            }catch (Exception ex){
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setMessage(ex.getMessage());
                b.show();
            }
            Toast.makeText(this,"Dados salvos",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,DashboardActivity.class));
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

    public Date stringFormatParaData(String data) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = formatter.parse(data);
        return d;
    }

    public void preencerDadosViagem() throws ParseException {

        viagem = new Viagem();
        viagem.setDestino(destino.getText().toString());
        viagem.setOrcamento(Double.parseDouble(orcamento.getText().toString()));
        viagem.setQuantidade_pessoas(Integer.parseInt(quantidadePessoas.getText().toString()));
        viagem.setData_partida(stringFormatParaData(btnDataPartida.getText().toString()));
        viagem.setData_chegada(stringFormatParaData(btnDataChegada.getText().toString()));
        if(tipo == R.id.rbtnLazer)
            viagem.setTipo(1);
        else
            viagem.setTipo(2);



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
            Date dateTime = calendar.getTime();

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            String data = dateFormat.format(dateTime);

            btnData.setText(data);
        }
    }
}
