package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinhasViagensActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
                                                                        DialogInterface.OnClickListener {

    private ListView listaViagens;
    private ArrayList<HashMap<String,Object>> viagens;
    private AlertDialog alertDialog;
    private AlertDialog confirmDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_viagens);

        listaViagens = (ListView) findViewById(R.id.listaViagens);

        String [] de = {"destino","data","valor","barraProgresso"};
        int [] para ={R.id.txtDestino, R.id.txtData, R.id.txtValor,R.id.barraProgresso};


        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listarViagem(),R.layout.viagem_lists,de,para);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Popullar a minha lista
     private List<HashMap<String, Object>> listarViagem(){

        viagens = new ArrayList<HashMap<String,Object>>();

        HashMap<String,Object> item = new HashMap<String,Object>();
         item.put("destino","Fortaleza");
         item.put("data","10/10/10");
         item.put("valor", "300,00");
         item.put("barraProgresso",  new Double[]{500.0,450.0,314.98});
         viagens.add(item);

        item = new HashMap<String, Object>();
         item.put("destino", "Maceió");
         item.put("data","10/10/10");
         item.put("valor", "Gasto total R$ 25.834,67");
         item.put("barraProgresso",  new Double[]{500.0,450.0,314.98});
         viagens.add(item);



        return viagens;
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
        this.alertDialog = criaAlertDialog();
        alertDialog.show();



       /* HashMap<String, String> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: "+ destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListaActivity.class));*/
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case 0:
                startActivity(new Intent(this, CadastroViagemActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, GastoViagemActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListaActivity.class));
                break;
            case 3:
                confirmDialog.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                Toast.makeText(this,"Item removido", Toast.LENGTH_SHORT).show();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this,"Item cancelado", Toast.LENGTH_SHORT).show();
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

                return true;
            }

            return false;
        }
    }
}
