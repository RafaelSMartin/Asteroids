package com.rafaels.asteroides.almacenPuntuaciones;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 02/12/2017.
 */

public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones {

    private static String PREFERENCIAS = "puntuaciones";
    private Context context;

    public AlmacenPuntuacionesPreferencias(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        // Nombre de fichero puntuaciones y modo de acceso tipo private
        // Pasamos el context por el constructor
        SharedPreferences preferencias = context.getSharedPreferences("puntuaciones",
                Context.MODE_PRIVATE);
        //Creamos un editor para añadirle contenido con putString con una clave llamada puntuacion
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("puntuacion", puntos + "" + nombre);
        //Para hacer que se refleje el contenido añadido (apply o commit->devuelve exito o no)
        editor.apply();

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<>();
        SharedPreferences preferencias = context.getSharedPreferences("puntuaciones",
                Context.MODE_PRIVATE);
        //Leo un string con clave puntuacion con valor por defecto si algo falla ""
        String s = preferencias.getString("puntuacion", "");
        // Si esa puntuacion existe añadimos el resultado al vector y lo devolvemos
        if(!s.equals("")){
            result.add(s);
        }
        return result;
    }
}
