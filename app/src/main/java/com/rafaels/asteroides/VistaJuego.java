package com.rafaels.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael S Martin on 22/10/2017.
 */

public class VistaJuego extends View {

    ////// ASTEROIDES //////
    private List<Grafico> asteroides;   // Lista con los Asteroides
    private int numAsteroides = 5;      // Número inicial de asteroides
    private int numFragmentos = 3;      // Fragmentos en que se divide

    ////// NAVE //////
    private Grafico nave;           // Gráfico de la nave
    private int giroNave;           // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad
    private static final int MAX_VELOCIDAD_NAVE = 20;
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;

        SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
            //Desactivo aceleracion grafica
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            Path pathNave = new Path();
            pathNave.moveTo((float) 0.0, (float) 0.0);
            pathNave.lineTo((float) 1.2, (float) 0.65);
            pathNave.lineTo((float) 0.0, (float) 1.2);
            pathNave.lineTo((float) 0.0, (float) 0.0);
            ShapeDrawable dNave = new ShapeDrawable(
                    new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(50);
            dNave.setIntrinsicHeight(50);
            drawableNave = dNave;

            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);
            ShapeDrawable dAsteroide = new ShapeDrawable(
                    new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;

            setBackgroundColor(Color.BLACK);
        } else if(pref.getString("graficos", "1").equals("3")){
            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.asteroide1);
            ContextCompat.getDrawable(context, R.drawable.asteroide1);

            drawableNave =
                    context.getResources().getDrawable(R.drawable.estrella);
            ContextCompat.getDrawable(context, R.drawable.estrella);
        }
        else {
            //Activo aceleracion grafica
            setLayerType(View.LAYER_TYPE_HARDWARE, null);

            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.asteroide1);
            ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableNave =
                    context.getResources().getDrawable(R.drawable.nave);
            ContextCompat.getDrawable(context, R.drawable.nave);
        }





        asteroides = new ArrayList<Grafico>();
        nave = new Grafico(this, drawableNave);

        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto,
                                           int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.
        for (Grafico asteroide: asteroides) {
            do {
                asteroide.setCenX((int) (Math.random()*ancho));
                asteroide.setCenY((int) (Math.random()*alto));
            } while(asteroide.distancia(nave) < (ancho+alto)/5);
        }
        nave.setCenX((int) (ancho/2));
        nave.setCenY((int) (alto/2));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide: asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        nave.dibujaGrafico(canvas);
    }
}