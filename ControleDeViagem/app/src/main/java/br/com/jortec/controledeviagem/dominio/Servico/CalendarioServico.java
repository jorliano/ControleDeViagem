package br.com.jortec.controledeviagem.dominio.Servico;

import android.app.usage.UsageEvents;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import br.com.jortec.controledeviagem.dominio.entidade.Viagem;
import br.com.jortec.controledeviagem.dominio.util.Constantes;

/**
 * Created by Jorliano on 17/09/2015.
 */
public class CalendarioServico {

    private Calendar calendar;
    private String nomeConta;

    public  CalendarioServico(String nomeConta,String tokenAcesso){
        this.nomeConta = nomeConta;
        GoogleCredential credential = new GoogleCredential();
        credential.setAccessToken(tokenAcesso);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = new GsonFactory();


        calendar = new Calendar.Builder(transport, jsonFactory, credential)
                                       .setApplicationName(Constantes.NOME_APLICATIVO)
                                       .build();

    }

    public  void cirarEvento(Viagem viagem){

        Event evento = new Event();

        evento.setSummary(viagem.getDestino());

        List<EventAttendee> participantes = Arrays.asList(new EventAttendee().setEmail(nomeConta));
        evento.setAttendees(participantes);

        DateTime inicio = new DateTime(viagem.getData_partida(), TimeZone.getDefault());
        DateTime fim    = new DateTime(viagem.getData_chegada(), TimeZone.getDefault());

        evento.setStart(new EventDateTime().setDateTime(inicio));
        evento.setEnd(new EventDateTime().setDateTime(fim));

        try {
            Event eventoCriado = calendar.events().insert(nomeConta,evento).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}



