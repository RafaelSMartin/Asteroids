package com.rafaels.asteroides.almacenPuntuaciones;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 03/12/2017.
 */

public class AlmacenPuntuacionesFicheroExterno implements AlmacenPuntuaciones {

    private static String FICHERO = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";
    private Context context;

    public AlmacenPuntuacionesFicheroExterno(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try {
            FileOutputStream file = new FileOutputStream(FICHERO, true);
            String texto = puntos + "" + nombre + "\n";
            file.write(texto.getBytes());
            file.close();
        }catch (Exception e){
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        try{
            FileInputStream file = new FileInputStream(FICHERO);
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
