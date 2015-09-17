package br.com.jortec.controledeviagem.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;

import br.com.jortec.controledeviagem.DashboardActivity;
import br.com.jortec.controledeviagem.R;

/**
 * Created by Jorliano on 13/09/2015.
 */
public class BarraNotificacoes {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void criarNotificacao(Context context,String titulo,CharSequence mensagemBarraStatus){


        // PendingIntent para executar a Activity se o usuário selecionar a notificação
        PendingIntent  pendingIntent = PendingIntent.getActivity(context,0,new Intent(context,DashboardActivity.class),0);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setTicker("Alerta");
        builder.setContentTitle(titulo);
        builder.setContentText(mensagemBarraStatus);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notification.flags= Notification.FLAG_AUTO_CANCEL;
        notification.defaults|= Notification.DEFAULT_VIBRATE;
        notification.defaults|= Notification.DEFAULT_LIGHTS;
        notification.defaults|= Notification.DEFAULT_SOUND;


        NotificationManager notificationManager = (NotificationManager)  context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.app_name, notification);

    }


}
