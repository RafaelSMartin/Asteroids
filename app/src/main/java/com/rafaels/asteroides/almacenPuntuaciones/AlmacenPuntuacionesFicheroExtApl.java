package com.rafaels.asteroides.almacenPuntuaciones;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.rafaels.asteroides.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 03/12/2017.
 */

public class AlmacenPuntuacionesFicheroExtApl implements AlmacenPuntuaciones {

    File path;
    File FILE;

    private Context context;

    public AlmacenPuntuacionesFicheroExtApl(Context context){
        this.context = context;
        createdExternalStoragePublic();
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        String stadoSD = Environment.getExternalStorageState();
        if(!stadoSD.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(context, "No puedo escribir en la memoria externa", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileOutputStream file = new FileOutputStream(FILE, true);
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

        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED) && !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(context, "No puedo leer en la memoria externa", Toast.LENGTH_LONG).show();
            return result;
        }

        try{
            FileInputStream file = new FileInputStream(FILE);
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

    private void createdExternalStoragePublic(){
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        FILE = new File(path, "puntuaciones.txt");

        if(!path.exists()){
            path.mkdirs();
        }
    }


}
