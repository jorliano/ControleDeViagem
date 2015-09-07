package br.com.jortec.controledeviagem;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadastroViagemActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnDataPartida;
    private Button btnDataChegada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_viagem);

        btnDataChegada = (Button) findViewById(R.id.btnDataChegada);
        btnDataPartida = (Button) findViewById(R.id.btnDataPartida);

        btnDataChegada.setOnClickListener(this);
        btnDataPartida.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_viagem, menu);
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

    @Override
    public void onClick(View v) {

        if(v == btnDataChegada)
            exibirData(btnDataChegada);
        else
            exibirData(btnDataPartida);
    }

    public void exibirData(Button btnData){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,new SeleciionaDataPicker(btnData),ano,mes,dia);
        dpd.show();
    }

    private class  SeleciionaDataPicker implements DatePickerDialog.OnDateSetListener{
        Button btnData;

        public SeleciionaDataPicker(Button btnData){
            this.btnData = btnData;
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            Date date = calendar.getTime();

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            String data = dateFormat.format(date);

            btnData.setText(data);
        }
    }
}
