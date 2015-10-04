package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
import br.com.jortec.controledeviagem.dominio.Servico.CalendarioServico;
import br.com.jortec.controledeviagem.dominio.Servico.ImagemServico;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;
import br.com.jortec.controledeviagem.dominio.util.Constantes;
import br.com.jortec.controledeviagem.dominio.util.Formate;

public class CadastroViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Button btnDataPartida;
    private Button btnDataChegada;
    private Button btnSalvar;
    private ImageButton btnFoto;
    private EditText destino;
    private EditText orcamento;
    private EditText quantidadePessoas;
    private EditText urlFoto;
    private RadioGroup tipoSelecionado;
    private int tipo;
    private int id = 0;
    private String nomeImagem;



    ViagemDao dao;
    Viagem viagem;
    CalendarioServico calendarioServico;
    private SharedPreferences preferences;
    private Uri uri;
    public final int CAPITURA_IMAGEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagem);

        dao = new ViagemDao(this);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.viagem_cadastro_toolbar);
        toolbar.setTitle("Viagens");
        toolbar.setSubtitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //Associar valores do meu layout de viagens
        btnDataChegada = (Button) findViewById(R.id.btnDataChegada);
        btnDataPartida = (Button) findViewById(R.id.btnDataPartida);
        btnSalvar = (Button) findViewById(R.id.btnSalvarViagem);
        btnFoto = (ImageButton) findViewById(R.id.btnFoto);
        destino = (EditText) findViewById(R.id.edtDestino);
        orcamento = (EditText) findViewById(R.id.edtOrcamento);
        quantidadePessoas = (EditText) findViewById(R.id.edtPessoas);
        urlFoto = (EditText) findViewById(R.id.edtUrl);
        tipoSelecionado =(RadioGroup) findViewById(R.id.radioGoup);

        tipo = tipoSelecionado.getCheckedRadioButtonId();



        //Eventos dos componentes
        btnDataChegada.setOnClickListener(this);
        btnDataPartida.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        calendarioServico = criarCalendarioServico();

        //pega o id da viagem selecionada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(MinhasViagensActivity.VIAGEM_ID)){
            toolbar.setSubtitle("Editar");
            id = bundle.getInt(MinhasViagensActivity.VIAGEM_ID);
            carregarViagemSelecionada();

        }else{
            viagem = new Viagem();
        }



    }
    public CalendarioServico criarCalendarioServico(){
        preferences = getSharedPreferences(Constantes.PREFERENCIA,MODE_PRIVATE);
        String nomeConta = preferences.getString(Constantes.NOME_CONTA, null);
        String tokenAcesso = preferences.getString(Constantes.TOKEN_ACESSO,null);

        Toast.makeText(this,"nome da conta: "+nomeConta,Toast.LENGTH_LONG).show();


        return new CalendarioServico(nomeConta,tokenAcesso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAPITURA_IMAGEM){
            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Mostrar a imagem",Toast.LENGTH_SHORT);
                adicionarGaleria();
                urlFoto.setText(nomeImagem);

            }
            else{
                Toast.makeText(this,"A imagem não foi capiturada ",Toast.LENGTH_SHORT);
            }
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
        if (id == android.R.id.home) {
            finish();
        }

        return true;
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
                    dao.salvarViagem(this, viagem);
                    new Task().execute(viagem);
                    startActivity(new Intent(this, MinhasViagensActivity.class));
                }else{
                    preencerDadosViagem();
                    dao.editarViagem(this, viagem, id);
                    startActivity(new Intent(this, MinhasViagensActivity.class));
                }
                break;
            case R.id.btnFoto:
                 capitura();
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
        viagem.setImagem(urlFoto.getText().toString());
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

            urlFoto.setText(viagem.getImagem());

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

    private class Task extends AsyncTask<Viagem, Void, Void>{

        @Override
        protected Void doInBackground(Viagem... viagens) {
            Viagem viagem = viagens[0];
            calendarioServico.cirarEvento(viagem);

            return null;
        }
    }

    public void capitura(){
        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        nomeImagem = diretorio.getPath()+"/"+System.currentTimeMillis()+".jpg";

        uri = Uri.fromFile(new File(nomeImagem));

        //Chamar a função da camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,CAPITURA_IMAGEM);


    }
    public void adicionarGaleria(){

        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(uri);
        this.sendBroadcast(i);
    }
}
