package br.com.jortec.controledeviagem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText usuario ;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.edtUsuario);
        senha   = (EditText) findViewById(R.id.edtSenha);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void onClickLogar(View view) {
        String usuarioInformado = usuario.getText().toString();
        String senhaInformado = senha.getText().toString();

        if("jorliano".equals(usuarioInformado) && "leandro".equals(senhaInformado)){

            startActivity(new Intent(this,DashboardActivity.class));
        }
        else {
            String msgErro = getString(R.string.erroAutenticacao);
            Toast toast = Toast.makeText(this,msgErro,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
