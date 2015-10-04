package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.jortec.controledeviagem.app.BarraNotificacoes;
import br.com.jortec.controledeviagem.database.DatabaseSql;
import br.com.jortec.controledeviagem.dominio.Dao.ViagemDao;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;
import br.com.jortec.controledeviagem.dominio.entidade.Viagem;

public class MinhasViagensActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
                                                                        DialogInterface.OnClickListener {
    private Toolbar toolbar;
    private ListView listaViagens;
    private ArrayList<HashMap<String,Object>> viagens;
    private AlertDialog alertDialog;
    private AlertDialog confirmDialog;

    public static String VIAGEM_ID = "viagem_id";

    private ViagemDao dao;

    int idViagem;
    int viagemSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_viagens);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.viagens_toolbar);
        toolbar.setTitle("Minhas Viagens");
        toolbar.setSubtitle("lista");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Pegar as preferencias da viagem
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String  valor = preferences.getString("valor_limite", "-1");
        Double valorLimite = Double.parseDouble(valor);


         dao = new ViagemDao(this);

        listaViagens = (ListView) findViewById(R.id.listaViagens);
        viagens =  dao.listarViagem(this,valorLimite);



         String [] de = {"imagem","destino","data","totalGasto","barraProgresso"};
         int [] para ={R.id.txtImagem,R.id.txtDestino, R.id.txtData, R.id.txtValorViagem, R.id.barraProgresso};




        SimpleAdapter simpleAdapter = new SimpleAdapter(this,viagens,R.layout.viagem_lists,de,para);
        simpleAdapter.setViewBinder(new ViagemViewBinder());

        listaViagens.setAdapter(simpleAdapter);
        listaViagens.setOnItemClickListener(this);
        this.confirmDialog = criaDialogConfirmacao();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_minhas_viagens, menu);
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

    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = { getString(R.string.editar),
                                       getString(R.string.novo_gasto),
                                       getString(R.string.gastos_realizados),
                                       getString(R.string.deletar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);
        return builder.create();
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);
        return builder.create();
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        idViagem  = (int) viagens.get(position).get("_id");

        viagemSelecionada = position;

        this.alertDialog = criaAlertDialog();
        alertDialog.show();
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case 0://Iniciar o cadastro de uma nova viagem
                Intent itCadastro = new Intent(this, CadastroViagemActivity.class);
                itCadastro.putExtra(Gasto.VIAGEM_ID, idViagem);
                startActivity(itCadastro);
                break;
            case 1://Iniciar o novo gasto da viagem selecionada
                Intent it = new Intent(this, GastoViagemActivity.class);
                it.putExtra(Gasto.VIAGEM_ID, idViagem);
                startActivity(it);
                break;
            case 2://Lista gasstos das viagens selecionada
                Intent itente = new Intent(this, GastoListaActivity.class);
                itente.putExtra(VIAGEM_ID, idViagem);
                startActivity(itente);
                break;
            case 3://Chama o dialogo de confirmação
                confirmDialog.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);//remover da lista
                dao.deletarViagem(this, idViagem);
                listaViagens.invalidateViews();//destruir a lista
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }

    public class ViagemViewBinder implements SimpleAdapter.ViewBinder{

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if(view.getId() == R.id.barraProgresso) {

                Double valores[] = (Double[]) data;
                ProgressBar progressoBar = (ProgressBar) view;
                progressoBar.setMax(valores[0].intValue());
                progressoBar.setSecondaryProgress(valores[1].intValue());
                progressoBar.setProgress(valores[2].intValue());


                if(valores[2] > valores[1]){
                    BarraNotificacoes.criarNotificacao(view.getContext(), "Controle de Viagem", "Gastos ultrapassaram o valor limite");
                }

               return true;
            }
            return false;
        }
    }
}
