package br.com.jortec.controledeviagem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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

    public void selecionarOpcao(View view) {

        TextView opcao = (TextView) view;
        String op = opcao.getText().toString();

        String viagem = getString(R.string.nova_viagem);
        String gasto = getString(R.string.novo_gasto);
        String minhas_viagem = getString(R.string.minhas_viagens);


       if(viagem.equals(op)){
           startActivity(new Intent(this,CadastroViagemActivity.class));
        }
        else
           if(gasto.equals(op)){
                startActivity(new Intent(this,GastoViagemActivity.class));
            }
            else
                if(minhas_viagem.equals(op)){
                    startActivity(new Intent(this,MinhasViagensActivity.class));
                }
                else {
                    Toast toast = Toast.makeText(this,"Selecionado "+op,Toast.LENGTH_SHORT);
                    toast.show();
                }

    }
}
