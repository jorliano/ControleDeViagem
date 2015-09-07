package br.com.jortec.controledeviagem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastoListaActivity extends AppCompatActivity {
    private ListView listaGastos;
    private ArrayList<HashMap<String,Object>> listarGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_lista);

        listaGastos = (ListView) findViewById(R.id.listaGastos);

        String [] de = {"data","descricao","valor","categoria"};
        int [] para ={R.id.txtDataGasto,R.id.txtDescricaoGasto,R.id.txtValorGasto,R.id.lytCategoria};


        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listar(),R.layout.gasto_list,de,para);
        simpleAdapter.setViewBinder(new GastoViewBinder());

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

    private ArrayList<HashMap<String,Object>> listar() {
        listarGastos = new ArrayList<HashMap<String,Object>>();

        HashMap<String,Object> item = new HashMap<String,Object>();
        item.put("data","10/08/2012");
        item.put("descricao", "Sanduiche");
        item.put("valor","Gasto : R$ 5,00");
       item.put("categoria", (R.color.categoria_alimentacao));
        listarGastos.add(item);

        item = new HashMap<String,Object>();
        item.put("data","11/08/2012");
        item.put("descricao", "Roupa");
        item.put("valor","Gasto : R$ 30,00");
        item.put("categoria", (R.color.categoria_hospedagem));
        listarGastos.add(item);

        item = new HashMap<String,Object>();
        item.put("data","12/08/2012");
        item.put("descricao", "Almoço");
        item.put("valor", "Gasto : R$ 20,00");
        item.put("categoria", (R.color.categoria_transporte));
        listarGastos.add(item);

        item = new HashMap<String,Object>();
        item.put("data","12/08/2012");
        item.put("descricao", "local");
        item.put("valor", "Gasto : R$ 20,00");
        item.put("categoria", (R.color.categoria_outros));
        listarGastos.add(item);

      return listarGastos;
    }

    public class GastoViewBinder implements SimpleAdapter.ViewBinder{
        private String dataAnterior ="";

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if(view.getId() == R.id.txtDataGasto) {
                if (!dataAnterior.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);

                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }

           if(view.getId() == R.id.lytCategoria){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }

          return false;
        }
    }
}
