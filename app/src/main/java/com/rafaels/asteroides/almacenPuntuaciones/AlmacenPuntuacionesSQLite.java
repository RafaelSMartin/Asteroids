package com.rafaels.asteroides.almacenPuntuaciones;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by Rafael S Martin on 16/12/2017.
 */

public class AlmacenPuntuacionesSQLite extends SQLiteOpenHelper implements AlmacenPuntuaciones {

    public AlmacenPuntuacionesSQLite(Context context) {
        super(context, "puntuaciones", null, 1);
    }

    //Metodos de SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE puntuaciones ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "puntos INTEGER, nombre TEXT, fecha BIGINT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de una nueva versión habría que actualizar las tablas

    }

    //Metodos de AlmacenPuntuaciones
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO puntuaciones VALUES ( null, "+
                puntos+", '"+nombre+"', "+fecha+")");
        db.close();
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        SQLiteDatabase db = getReadableDatabase();

        //Consulta rawQuery
//        Cursor cursor = db.rawQuery(
                            // "SELECT puntos, nombre
                            // FROM " + "puntuaciones
                            // ORDER BY puntos
                            // DESC LIMIT " +cantidad,
                            // null);
        //Consulta query
        String[] CAMPOS = {"puntos", "nombre"};
        Cursor cursor=db.query(
                "puntuaciones", //tabla a consultar (FROM)
                CAMPOS, //columnas a devolver (SELECT)
                null, //consulta (WHERE)
                null, //reemplaza "?" de seleccion
                null, //agrupado por (GROUPBY)
                null, //condicion de f. aritmetica
                "puntos DESC", //ordenado por
                Integer.toString(cantidad)); //cantidad max. de registros

        while (cursor.moveToNext()){
            result.add(cursor.getInt(0)+" " +cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}
