package com.rafaels.asteroides.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;


import com.rafaels.asteroides.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Rafael S Martin on 13/10/2017.
 */

public class PreferenciasFragment extends PreferenceFragment {

    private String text;
    private int duration;
    private Snackbar snackBar;
    private int gravity, xOffset, yOffset;
    private EditTextPreference fragmentos;
    private CheckBoxPreference sensores;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        fragmentos = (EditTextPreference)findPreference("fragmentos");

        //Capturo y muestro el valor por defecto de las preferencias
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String string_default = pref.getString("fragmentos","?");
        fragmentos.setSummary( "En cuantos trozos se divide un asteroide ("+string_default+")");

        fragmentos.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int valor;
                try {
                    valor = Integer.parseInt((String)newValue);
                } catch(Exception e) {
//                    No saldra por ser inputType="number" XD o si falla el try
//                    Toast.makeText(getActivity(), "Ha de ser un número", Toast.LENGTH_SHORT).show();
                    String texto = getString(R.string.snackbar_text_error);
                    customSnackBar(getView(), texto);
                    return false;
                } if (valor>=0 && valor<=9) {
                    fragmentos.setSummary( "En cuantos trozos se divide un asteroide ("+valor+")");
                    return true;
                } else {
//                    Toast.makeText(getActivity(), "Máximo de fragmentos 9", Toast.LENGTH_SHORT).show();
                    String texto = getString(R.string.snackbar_text_max_fragmentos);
                    customSnackBar(getView(), texto);
                    return false;
                }
            }
        });

    }//Fin onCreate


    public void customSnackBar(View view, String texto){

        text = texto;
        duration = (int) TimeUnit.SECONDS.toMillis(4); //Sirve para el SnackBar

        snackBar = Snackbar.make(view, text, duration);

        // Obtén la vista del snackbar
        View snackbarView = snackBar.getView();
        //Cambia el color del SnackBar
        int snackBarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackBarTextId);
        textView.setTextColor(Color.WHITE);
        // Cambia el fondo del snackbar
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        snackBar
                .setActionTextColor(Color.WHITE)
                .setAction(getString(R.string.aceptar), new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //tu accion aqui, camba el toast de lugar
//                        gravity = Gravity.CENTER;
//                        xOffset = 0;
//                        yOffset = 0;
//                        Toast toast = Toast.makeText(getActivity(), "onClick Action", Toast.LENGTH_SHORT);
//                        toast.setGravity(gravity, xOffset, yOffset);
//                        toast.show();

                    }
                });

        snackBar.setCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar snackbar, int event){
                //Tu accion dismiss aqui
//                Toast toast = Toast.makeText(getActivity(), "onDimissed", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER|Gravity.LEFT, 0, 0);
//                toast.show();
            }
            @Override
            public void onShown(Snackbar snackbar){
                //Tu accion cuando se muestre el snackbar aqui
//                Toast.makeText(getActivity(), "onShown", Toast.LENGTH_SHORT).show();
            }
        });
        snackBar.show();
    }



}//Fin
