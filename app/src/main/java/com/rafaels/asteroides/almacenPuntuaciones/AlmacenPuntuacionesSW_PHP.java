package com.rafaels.asteroides.almacenPuntuaciones;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 19/12/2017.
 */

public class AlmacenPuntuacionesSW_PHP implements AlmacenPuntuaciones {

    HttpURLConnection conexion;

    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        try {
            URL url=new URL("http://192.168.1.141/puntuaciones/lista.php" + "?max=20");
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (!linea.equals("")) {
                    result.add(linea);
                    linea = reader.readLine();
                }
                reader.close();
            } else {
                Log.e("Asteroides", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (conexion!=null) conexion.disconnect();
            return result;
        }
    }

    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try {
            URL url=new URL("http://192.168.1.141/puntuaciones/nueva.php?" + "puntos="+ puntos + "&nombre="+ URLEncoder.encode(nombre, "UTF-8") + "&fecha=" + fecha);
            HttpURLConnection conexion = (HttpURLConnection) url
                    .openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                if (!linea.equals("OK")) {
                    Log.e("Asteroides","Error en servicio Web nueva");
                }
            } else {
                Log.e("Asteroides", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (conexion!=null) conexion.disconnect();
        }
    }
}