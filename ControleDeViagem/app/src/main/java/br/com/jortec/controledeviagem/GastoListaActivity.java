package br.com.jortec.controledeviagem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastoListaActivity extends AppCompatActivity {
    private ListView listaGastos;
    private ArrayList<HashMap<String,String>> listarGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_lista);

        listaGastos = (ListView) findViewById(R.id.listaGastos);

        String [] de = {"descricao","valor"};
        int [] para ={android.R.id.text1,android.R.id.text2};


        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listar(),android.R.layout.two_line_list_item,de,para);

        listaGastos.setAdapter(simpleAdapter);
        //listaGastos.setOnItemClickListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gasto_lista, menu);
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

    private ArrayList<HashMap<String,String>> listar() {
        listarGastos = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item = new HashMap<String,String>();
        item.put("descricao","Sanduiche");
        item.put("valor","Gasto : R$ 5,00");
        listarGastos.add(item);

        item = new HashMap<String,String>();
        item.put("descricao","Roupa");
        item.put("valor","Gasto : R$ 30,00");
        listarGastos.add(item);

        item = new HashMap<String,String>();
        item.put("descricao","Almoço");
        item.put("valor", "Gasto : R$ 20,00");
        listarGastos.add(item);

      return listarGastos;
    }
}
