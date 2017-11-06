package com.rafaels.asteroides.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rafaels.asteroides.R;
import com.rafaels.asteroides.graphics.VistaJuego;

/**
 * Created by Rafael S Martin on 22/10/2017.
 */

public class Juego extends AppCompatActivity {

    private VistaJuego vistaJuego;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);

        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        vistaJuego.activarSensorOrientacion();
        vistaJuego.getThread().reanudar();
    }

    @Override
    protected void onPause(){
        vistaJuego.getThread().pausar();
        vistaJuego.desactivarSensorOrientacion();
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
