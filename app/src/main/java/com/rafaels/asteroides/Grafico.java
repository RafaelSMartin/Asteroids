package com.rafaels.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Rafael S Martin on 22/10/2017.
 */

public class Grafico {

    private Drawable drawable;  //Imagen que dibujaremos
    private int cenX, cenY;     //Posicion del centro del grafico
    private int ancho, alto;    //Dimensiones de la imagen
    private double incX, incY;  //Velocidad desplazamiento
    private double angulo, rotacion;    //Angulo y velocidad rotacion
    private int radioColision;          //Para determinar colisión
    private int xAnterior, yAnterior;   //Posicion anterior
    private int radioInval;     //Radio usado en invalidate()
    private View view;          //Usada en View.invalidate()

    public Grafico(View view, Drawable drawable){
        this.view = view;
        this.drawable = drawable;
        ancho = drawable.getIntrinsicWidth();
        alto = drawable.getIntrinsicHeight();
        radioColision = (alto+ancho)/4;
        radioInval = (int) Math.hypot(ancho/2, alto/2);
    }

    public void dibujaGrafico(Canvas canvas){
        int x = cenX - ancho/2;
        int y = cenY - alto/2;
        //Situa los limites donde ira el drawable
        drawable.setBounds(x, y, x+ancho, y+alto);
        canvas.save();
        //Aplica transformacion de rotacion
        canvas.rotate((float)angulo, cenX, cenY);
        //Dibuja el drawable en el canvas
        drawable.draw(canvas);
        //Recupera la matriz de transformacion introducida para q no vuelva a operar
        canvas.restore();
        //Redibujamos la vista recordando la rotacion
        view.invalidate(cenX-radioInval, cenY-radioInval,
            cenX+radioInval, cenY+radioInval);
        view.invalidate(xAnterior-radioInval, yAnterior-radioInval,
                xAnterior+radioInval, yAnterior+radioInval);
        xAnterior = cenX;
        yAnterior = cenY;
    }

    //Modifica la velocidad segun el factor, 1->normal, 2->doble ...
    public void incrementaPos(double factor){
        cenX += incX * factor;
        cenY += incY * factor;
        angulo += rotacion * factor;
        //Si salimos de pantalla corregimos la posicion
        if(cenX < 0){
            cenX = view.getWidth();
        }
        if(cenX > view.getWidth()){
            cenX = 0;
        }
        if(cenY < 0){
            cenY = view.getHeight();
        }
        if(cenY > view.getHeight()){
            cenY = 0;
        }
    }

    public double distancia(Grafico g){
        return Math.hypot(cenX-g.cenX, cenY-g.cenY);
    }

    public boolean verificaColision(Grafico g){
        return (distancia(g) < (radioColision + g.radioColision));
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getCenX() {
        return cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public double getIncX() {
        return incX;
    }

    public double getIncY() {
        return incY;
    }

    public double getAngulo() {
        return angulo;
    }

    public double getRotacion() {
        return rotacion;
    }

    public int getRadioColision() {
        return radioColision;
    }

    public int getxAnterior() {
        return xAnterior;
    }

    public int getyAnterior() {
        return yAnterior;
    }

    public int getRadioInval() {
        return radioInval;
    }

    public View getView() {
        return view;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

    public void setRadioColision(int radioColision) {
        this.radioColision = radioColision;
    }

    public void setxAnterior(int xAnterior) {
        this.xAnterior = xAnterior;
    }

    public void setyAnterior(int yAnterior) {
        this.yAnterior = yAnterior;
    }

    public void setRadioInval(int radioInval) {
        this.radioInval = radioInval;
    }

    public void setView(View view) {
        this.view = view;
    }
}
