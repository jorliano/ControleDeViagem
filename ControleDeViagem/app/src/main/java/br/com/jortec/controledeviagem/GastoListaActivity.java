package br.com.jortec.controledeviagem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import br.com.jortec.controledeviagem.dominio.Dao.GastoDao;
import br.com.jortec.controledeviagem.dominio.entidade.Gasto;

public class GastoListaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
                                                                     Dialog.OnClickListener{
    private ListView listaGastos;
    private ArrayList<HashMap<String,Object>> listarGastos;
    private AlertDialog alertaDialog;
    private AlertDialog comfirmDialog;
    public static final String ID_GASTO ="id";
    Gasto gasto;
    GastoDao dao;

    int viagem_id;
    int idGasto;
    int gastoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_lista);

        dao = new GastoDao(this);

        listaGastos = (ListView) findViewById(R.id.listaGastos);

        String [] de = {"data","descricao","valor","categoria"};
        int [] para ={R.id.txtDataGasto,R.id.txtDescricaoGasto,R.id.txtValorGasto,R.id.lytCategoria};



        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.containsKey(MinhasViagensActivity.VIAGEM_ID)){
            viagem_id = bundle.getInt(MinhasViagensActivity.VIAGEM_ID);
            listarGastos = dao.listarGasto(this,viagem_id);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listarGastos,R.layout.gasto_list,de,para);
        simpleAdapter.setViewBinder(new GastoViewBinder());

        listaGastos.setAdapter(simpleAdapter);
        listaGastos.setOnItemClickListener(this);

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

    public AlertDialog criarDialogo(){
       final CharSequence[] itens = {"Editar",
                                     "Deletar",
                                     "Detalhes"};
        AlertDialog.Builder alg = new AlertDialog.Builder(this);
        alg.setTitle(R.string.opcoes);
        alg.setItems(itens,this);

        return  alg.create();
    }

    private AlertDialog criarDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_gasto);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);
        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         idGasto = (int) listarGastos.get(position).get("_id");
         gastoSelecionado = position;

         this.alertaDialog = criarDialogo();
         this.alertaDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0://Editar  gasto selecionado
                Intent itente = new Intent(this,GastoViagemActivity.class);
                itente.putExtra(ID_GASTO, idGasto);
                startActivity(itente);
                break;
            case 1://Deletar o Gasto selecionado
                this.comfirmDialog = criarDialogConfirmacao();
                comfirmDialog.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                  listarGastos.remove(gastoSelecionado);
                  dao.deletarGasto(this, idGasto);
                  listaGastos.invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
            case 3:
                break;
        }
    }


    public class GastoViewBinder implements SimpleAdapter.ViewBinder{
        private String dataAnterior ="";
        int cont;

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {


          /*  if(view.getId() == R.id.txtDataGasto) {


                if (!dataAnterior.equals(data)) {



                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);

                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);

                } else {

                    view.setVisibility(View.GONE);
                   // view.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(view.getContext(),"Metodo chamado "+cont, Toast.LENGTH_SHORT).show();

                return true;
            }*/

           if(view.getId() == R.id.lytCategoria){


              // Toast.makeText(view.getContext(),"Cor chamada "+id, Toast.LENGTH_SHORT).show();
               // view.setBackgroundColor(getResources().getColor(Integer.parseInt(id)));
                return true;
            }

          return false;
        }
    }
}
