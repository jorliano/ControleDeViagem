package br.com.jortec.controledeviagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Jorliano on 07/09/2015.
 */
public class ConfiguracoesActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
