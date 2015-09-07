package br.com.jortec.controledeviagem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinhasViagensActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listaViagens;
    private ArrayList<HashMap<String,String>> viagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_viagens);

        listaViagens = (ListView) findViewById(R.id.listaViagens);

        String [] de = {"destino","data","valor"};
        int [] para ={R.id.txtDestino, R.id.txtData, R.id.txtValor};


        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listarViagem(),R.layout.viagem_lists,de,para);

        listaViagens.setAdapter(simpleAdapter);
        listaViagens.setOnItemClickListener(this);

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
     private List<HashMap<String, String>> listarViagem(){

        viagens = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item = new HashMap<String,String>();
         item.put("destino","Fortaleza");
         item.put("data","10/10/10");
         item.put("valor", "300,00");
         viagens.add(item);

        item = new HashMap<String, String>();
         item.put("destino", "Maceió");
         item.put("data","10/10/10");
         item.put("valor","Gasto total R$ 25834,67");
         viagens.add(item);



        return viagens;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: "+ destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListaActivity.class));
    }
}
