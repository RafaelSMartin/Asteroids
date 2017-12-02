package com.rafaels.asteroides.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.rafaels.asteroides.R;
import com.rafaels.asteroides.graphics.VistaJuego;

/**
 * Created by Rafael S Martin on 22/10/2017.
 */

public class Juego extends AppCompatActivity {

    private VistaJuego vistaJuego;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
        vistaJuego.setPadre(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if((pref.getBoolean("sensores", true))){
            vistaJuego.activarSensorOrientacion();
            vistaJuego.activarSensorAcelerometro();
        }

        vistaJuego.getThread().reanudar();
    }

    @Override
    protected void onPause(){
        vistaJuego.getThread().pausar();

        if((pref.getBoolean("sensores", false))){
            vistaJuego.desactivarSensorOrientacion();
        }

        super.onPause();
    }

    @Override
    protected void onStop(){

        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        vistaJuego.getThread().detener();
        super.onDestroy();
    }



}
