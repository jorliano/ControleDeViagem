package br.com.jortec.controledeviagem.dominio.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jorliano on 12/09/2015.
 */
public class  Formate {


   public  static String doubleParaMonetario(double valor){

        NumberFormat format = new DecimalFormat(",##0,00");

        String valorFormatado = format.format(valor);

        return valorFormatado;
   }

    public  static Double MonetarioParaDouble(String valor){

        String convert = valor.replace(",","");
        convert.replace(".","");

        return Double.parseDouble(convert);
    }

    public static String dataParaString(Date data){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataFormatada = dateFormat.format(data);
        return  dataFormatada;
    }
    public static Date stringParaData(String data){
        Date dataFormatada = null;
       // DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            dataFormatada = dateFormat.parse(data);

        } catch (ParseException e) {
            e.printStackTrace();
        }
      return dataFormatada;
    }
}
