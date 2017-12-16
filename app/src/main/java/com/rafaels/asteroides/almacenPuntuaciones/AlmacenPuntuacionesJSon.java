package com.rafaels.asteroides.almacenPuntuaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 16/12/2017.
 */

public class AlmacenPuntuacionesJSon implements AlmacenPuntuaciones {
    private String string; //Almacena puntuaciones en formato JSON

    public AlmacenPuntuacionesJSon() {
        guardarPuntuacion(45000,"Mi nombreJSON", System.currentTimeMillis());
        guardarPuntuacion(31000,"Otro nombreJSON", System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        //string = leerString();
        Vector<Puntuacion> puntuaciones = leerJSon(string);
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = guardarJSon(puntuaciones);
        //guardarString(string);
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        //string = leerFichero();
        Vector<Puntuacion> puntuaciones = leerJSon(string);
        Vector<String> salida = new Vector<>();
        for (Puntuacion puntuacion: puntuaciones) {
            salida.add(puntuacion.getPuntos()+" "+puntuacion.getNombre());
        }
        return salida;
    }

    private String guardarJSon(Vector<Puntuacion> puntuaciones) {
        String string = "";
        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacion puntuacion : puntuaciones) {
                JSONObject objeto = new JSONObject();
                objeto.put("puntos", puntuacion.getPuntos());
                objeto.put("nombre", puntuacion.getNombre());
                objeto.put("fecha", puntuacion.getFecha());
                jsonArray.put(objeto);
            }
            string = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

    private Vector<Puntuacion> leerJSon(String string) {
        Vector<Puntuacion> puntuaciones = new Vector<>();
        try {
            JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                puntuaciones.add(new Puntuacion(objeto.getInt("puntos"),
                        objeto.getString("nombre"), objeto.getLong("fecha")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return puntuaciones;
    }
}
