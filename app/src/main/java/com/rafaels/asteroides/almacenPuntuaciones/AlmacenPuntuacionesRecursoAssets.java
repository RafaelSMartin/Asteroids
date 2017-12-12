package com.rafaels.asteroides.almacenPuntuaciones;

import android.content.Context;
import android.util.Log;

import com.rafaels.asteroides.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by Indogroup02 on 12/12/2017.
 */

public class AlmacenPuntuacionesRecursoAssets implements AlmacenPuntuaciones {

    private Context context;

    public AlmacenPuntuacionesRecursoAssets(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        try{
            InputStream file = context.getAssets().open("carpeta/puntuaciones.txt");
            BufferedReader entrada = new BufferedReader(new InputStreamReader(file));
            int n = 0;
            String linea;
            do{
                linea = entrada.readLine();
                if(linea != null){
                    result.add(linea);
                    n++;
                }
            } while(n < cantidad && linea != null);
            file.close();
        }catch (Exception e){
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }
}