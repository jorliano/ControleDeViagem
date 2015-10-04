package br.com.jortec.controledeviagem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

import br.com.jortec.controledeviagem.dominio.util.Constantes;


//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText usuario ;
    private EditText senha;
    private CheckBox manterConectado;
    private SharedPreferences preferences;
    private GoogleAccountManager accountManager;
    private Account conta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);


        usuario = (EditText) findViewById(R.id.edtUsuario);
        senha   = (EditText) findViewById(R.id.edtSenha);
        manterConectado =(CheckBox) findViewById(R.id.ckbConectado);

        preferences = getSharedPreferences(Constantes.PREFERENCIA,MODE_PRIVATE);
        boolean conectado = preferences.getBoolean(Constantes.MANTER_CONECTADO,false);

        accountManager = new GoogleAccountManager(this);

        if(conectado)
           solicitarAutorizacao();

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

        autentica(usuarioInformado,senhaInformado);
    }

    public void iniciarDashboard(){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void autentica(String nomeConta, String senha) {

        conta = accountManager.getAccountByName(nomeConta);
        if(conta == null){

            Toast.makeText(this,R.string.conta_inexistente,Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME,nomeConta);
        bundle.putString(AccountManager.KEY_PASSWORD, senha);

        accountManager.getAccountManager().confirmCredentials(conta, bundle, this, new AutenticacaoCallback(), null);

    }

    public void solicitarAutorizacao(){

        String tokenAcesso = preferences.getString(Constantes.TOKEN_ACESSO,null);
        String nomeConta = preferences.getString(Constantes.NOME_CONTA,null);

        //Invalida token de acesso
        if(tokenAcesso != null){
            accountManager.invalidateAuthToken(tokenAcesso);
            conta = accountManager.getAccountByName(nomeConta);
        }

        accountManager.getAccountManager().getAuthToken(conta,Constantes.AUTH_TOKEN_TYPE,null,this,
                new AutorizacaoCallback(),null);
    }

    //Retorno da credencial
    private class AutenticacaoCallback implements AccountManagerCallback {

        @Override
        public void run(AccountManagerFuture future) {
            try {
                Bundle bundle = (Bundle) future.getResult();

                //se validação for verdadeira
                if (bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {

                    solicitarAutorizacao();
                }
            } catch (OperationCanceledException e) { // usuário cancelou a operação
                e.printStackTrace();
            } catch (AuthenticatorException e) { // possível falha no autenticador
                e.printStackTrace();
            } catch (IOException e) { // possível falha de comunicação
                e.printStackTrace();
            }


        }
    }

    private class AutorizacaoCallback implements AccountManagerCallback{
        @Override
        public void run(AccountManagerFuture future) {
            try {
                Bundle bundle = (Bundle) future.getResult();
                String nomeConta = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                String tokenAcesso = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                gravarTokenAcesso(nomeConta, tokenAcesso);
                iniciarDashboard();

            } catch (OperationCanceledException e) {// usuário cancelou a operação
                e.printStackTrace();
            } catch (IOException e) { // possível falha de comunicação
                e.printStackTrace();
            } catch (AuthenticatorException e) {// possível falha no autenticador
                e.printStackTrace();
            }
        }

        public void gravarTokenAcesso(String nomeConta,String tokenAcesso){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constantes.NOME_CONTA,nomeConta);
            editor.putString(Constantes.TOKEN_ACESSO,tokenAcesso);
            editor.putBoolean(Constantes.MANTER_CONECTADO, manterConectado.isChecked());
            editor.commit();
        }
    }
}
