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
        for(int n = 9; n>=1; n--){
            editor.putString("puntuacion" + n,
                    preferencias.getString("puntuacion" + (n - 1), ""));
        }
        editor.putString("puntuacion0", puntos + " " + nombre);
        //Para hacer que se refleje el contenido añadido (apply o commit->devuelve exito o no)
        editor.apply();

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<>();
        SharedPreferences preferencias = context.getSharedPreferences("puntuaciones",
                Context.MODE_PRIVATE);
        //Leo un string con clave puntuacion con valor por defecto si algo falla ""
        for(int n = 0; n <= 9; n++){
            String s = preferencias.getString("puntuacion" + n, "");
            if (!s.isEmpty()){
                result.add(s);
            }
        }

        return result;
    }
}
