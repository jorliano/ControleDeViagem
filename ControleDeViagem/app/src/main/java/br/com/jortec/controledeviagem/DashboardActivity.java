package br.com.jortec.controledeviagem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jortec.controledeviagem.app.BarraNotificacoes;

public class DashboardActivity extends AppCompatActivity {
   private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle("Sistema");
        toolbar.setSubtitle("Escolha uma opção");
        setSupportActionBar(toolbar);


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

        switch (view.getId()){
            case R.id.nova_viagem:
                startActivity(new Intent(this,CadastroViagemActivity.class));
                break;
            case R.id.novo_gasto:
                startActivity(new Intent(this,GastoViagemActivity.class));
                break;
            case R.id.minhas_viagens:
                startActivity(new Intent(this,MinhasViagensActivity.class));
                break;
            case R.id.configuracao:
                startActivity(new Intent(this,ConfiguracoesActivity.class));
                break;
        }
    }
}
