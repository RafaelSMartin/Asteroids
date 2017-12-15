package com.rafaels.asteroides.almacenPuntuaciones;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 15/12/2017.
 */

public class AlmacenPuntuacionesGSon implements AlmacenPuntuaciones {

    private String string; //Almacena puntuaciones en formato JSON
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Puntuacion>>() {}.getType();

    public AlmacenPuntuacionesGSon() {
        guardarPuntuacion(45000,"Mi nombre", System.currentTimeMillis());
        guardarPuntuacion(31000,"Otro nombre", System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        //string = leerString();
        ArrayList<Puntuacion> puntuaciones;
        if (string == null) {
            puntuaciones = new ArrayList<>();
        } else {
        puntuaciones = gson.fromJson(string, type);
        }
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = gson.toJson(puntuaciones, type);
        //guardarString(string);
        }

        @Override
        public Vector<String> listaPuntuaciones(int cantidad) {
            //string = leerString();
            ArrayList<Puntuacion> puntuaciones;
            if (string == null) {
                puntuaciones = new ArrayList<>();
            } else {
                puntuaciones = gson.fromJson(string, type);
            }
            Vector<String> salida = new Vector<>();
            for (Puntuacion puntuacion : puntuaciones) {
                salida.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
            }
            return salida;
        }
}
