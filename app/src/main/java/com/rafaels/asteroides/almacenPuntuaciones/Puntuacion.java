package com.rafaels.asteroides.almacenPuntuaciones;

/**
 * Created by Rafael S Martin on 15/12/2017.
 * Modelo de datos POJO para serializar JSON
 *
 */

public class Puntuacion {

    private int puntos;
    private String nombre;
    private long fecha;

    public Puntuacion(int puntos, String nombre, long fecha){
        this.puntos = puntos;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}
