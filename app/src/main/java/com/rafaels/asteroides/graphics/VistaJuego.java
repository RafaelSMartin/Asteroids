package com.rafaels.asteroides.graphics;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.rafaels.asteroides.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Rafael S Martin on 22/10/2017.
 */

public class VistaJuego extends View implements SensorEventListener {

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

    // //// MISIL //////
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo = false;
    private int tiempoMisil;
//    private Vector<Grafico> misiles;
//    private Vector<Integer> tiempoMisiles;
//    private int numMisiles = 10;
//    Iterator vecInterator;

    ////// THREAD Y TIEMPO //////
    //Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    //Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    //Cuando se realizo el ultimo proceso
    private long ultimoProceso = 0;

    ////// EVENTOS //////
    private float mX = 0, mY = 0;
    private boolean disparo = false;

    ////// SENSORES //////
    private boolean hayValorInicial = false;
    private float valorInicial;
    SensorManager sm;
    private boolean isValorInicial;
    private boolean sensorActivo = false;

    ////// MULTIMEDIA //////
    SoundPool soundPool;
    int idDisparo, idExplosion;

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        idDisparo = soundPool.load(context, R.raw.disparo, 0);
        idExplosion = soundPool.load(context, R.raw.explosion, 0);


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
            dAsteroide.setIntrinsicWidth(150);
            dAsteroide.setIntrinsicHeight(150);
            drawableAsteroide = dAsteroide;

            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            drawableMisil = dMisil;

            setBackgroundColor(Color.BLACK);
        } else if (pref.getString("graficos", "1").equals("1")) {
            //Activo aceleracion grafica
            setLayerType(View.LAYER_TYPE_HARDWARE, null);

            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.asteroide1);
            ContextCompat.getDrawable(context, R.drawable.asteroide1);

            drawableNave =
                    context.getResources().getDrawable(R.drawable.nave);
            ContextCompat.getDrawable(context, R.drawable.nave);

            drawableMisil =
                    context.getResources().getDrawable(R.drawable.misil1);
            ContextCompat.getDrawable(context, R.drawable.misil1);

        } else {
            drawableAsteroide =
                    context.getResources().getDrawable(R.drawable.ic_asteroide1_vector_drawable);
            ContextCompat.getDrawable(context, R.drawable.ic_asteroide1_vector_drawable);

            drawableNave =
                    context.getResources().getDrawable(R.drawable.ic_nave_vector_drawable);
            ContextCompat.getDrawable(context, R.drawable.ic_nave_vector_drawable);

            drawableMisil =
                    context.getResources().getDrawable(R.drawable.misil1);
            ContextCompat.getDrawable(context, R.drawable.misil1);

            setBackgroundColor(Color.BLACK);
        }


        // Indicamos qué objeto recogerá la llamada callback
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (pref.getBoolean("sensores", true)) {
            activarSensorOrientacion();
        } else {
            desactivarSensorOrientacion();
        }


        asteroides = new ArrayList<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }

        nave = new Grafico(this, drawableNave);

        misil = new Grafico(this, drawableMisil);
//        misiles = new Vector<Grafico>();
//        for(int i = 0; i <numMisiles; i++){
//            Grafico misil = new Grafico(this, drawableMisil);
//            misiles.add(misil);
//        }
//        vecInterator = misiles.iterator();


    }

    @Override
    protected void onSizeChanged(int ancho, int alto,
                                 int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        nave.setCenX((int) (ancho / 2));
        nave.setCenY((int) (alto / 2));

        // Una vez que conocemos nuestro ancho y alto.
        for (Grafico asteroide : asteroides) {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            } while (asteroide.distancia(nave) < (ancho + alto) / 5);
        }

        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        nave.dibujaGrafico(canvas);
        synchronized (asteroides) {
            for (Grafico asteroide : asteroides) {
                asteroide.dibujaGrafico(canvas);
            }
        }
//        synchronized (misiles){
//            for(Grafico misil : misiles){
//                misil.dibujaGrafico(canvas);
//            }
//        }
        if (misilActivo) {
            misil.dibujaGrafico(canvas);
        }

    }

    protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        //No hacer nada si el periodo de proceso no se ha cumplido
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; // Salir si el período de proceso no se ha cumplido.
        }
        //Para una ejecucion en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        //Actualizamos posicion nave
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        double nIncX = nave.getIncX() + aceleracionNave *
                Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave *
                Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(retardo); // Actualizamos posición
        for (Grafico asteroide : asteroides) {
            asteroide.incrementaPos(retardo);
        }
//        for (Grafico misil : misiles){
//            misil.incrementaPos(retardo);
//            misil.setTiempoMisil(misil.getTiempoMisil() - (int) retardo);
//            if (misil.getTiempoMisil() < 0) {
//                misil.setMisilActivo(false);
//            } else {
//                for (int i = 0; i < asteroides.size(); i++) {
//                    if (misil.verificarColision(asteroides.get(i))) {
//                        destruyeAsteroide(i, misil);
//                        break;
//                    }
//                }
//            }
//        }
        // Actualizamos posición de misil
        if (misilActivo) {
            misil.incrementaPos(retardo);
            tiempoMisil -= retardo;
            if (tiempoMisil < 0) {
                misilActivo = false;
            } else {
                for (int i = 0; i < asteroides.size(); i++)
                    if (misil.verificaColision(asteroides.get(i))) {
                        destruyeAsteroide(i);
                        break;
                    }
            }
        }
    }

    private void destruyeAsteroide(int i) {
        synchronized (asteroides) {
            asteroides.remove(i);
            misilActivo = false;
        }
        this.postInvalidate();

        soundPool.play(idExplosion, 1,1,1,0,1);
    }

    private void activaMisil() {
        misil.setCenX(nave.getCenX());
        misil.setCenY(nave.getCenY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) *
                PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) *
                PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.
                getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;

        soundPool.play(idDisparo, 1,1,1,0,1);

        // centramos el misil atendiendo a la posición, altura y anchura de la
        // nave

//        misil.setCenX(nave.getCenX());
//        misil.setCenY(nave.getCenY());
//
//        misil.setAngulo(nave.getAngulo());
//        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
//                * PASO_VELOCIDAD_MISIL);
//        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
//                * PASO_VELOCIDAD_MISIL);
		/*
		 * Damos un tiempo de vida al misil para que no esté recorriendo
		 * permanentemente la pantalla hasta chocar con un asteroide. Nos
		 * quedamos con el mínimo entre el ancho dividido por la velocidad y el
		 * alto dividido por la velocidad y le restamos una constante
		 */

//        tiempoMisiles.set(tiempoMisiles, tiempoMisiles.get(tiempoMisiles)-retardo);

    }


    /**
     * EVENTOS de teclado y touch
     */

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
        super.onKeyDown(codigoTecla, evento);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                activaMisil();
                break;
            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }

    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
        super.onKeyUp(codigoTecla, evento);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;
            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx < 6 && dy > 6) {
                    aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo) {
                    activaMisil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }


    /**
     * SENSOR Orientacion
     */

    // Método implementado de la interfaz SensorEventListener
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float valor = sensorEvent.values[1];
        if (!hayValorInicial) {
            valorInicial = valor;
            hayValorInicial = true;
        }
        giroNave = (int) (valor - valorInicial) / 3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        this.isValorInicial = false;
    }

    // Registro del sensor
    public boolean activarSensorOrientacion() {
        List<Sensor> listaSensores = sm.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!listaSensores.isEmpty()) {
            Sensor sensorOrientacion = listaSensores.get(0);
            sm.registerListener(this, sensorOrientacion, sm.SENSOR_DELAY_GAME);
            return sensorActivo = true;
        } else
            return sensorActivo;

    }

    // Desactivar sensor
    public boolean desactivarSensorOrientacion() {
        if (sensorActivo) {
            sm.unregisterListener(this);
            sensorActivo = false;
        }
        return sensorActivo;
    }


    /**
     * THREAD del Juego
     */

    public class ThreadJuego extends Thread {

        private boolean pausa, corriendo;

        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public void detener() {
            corriendo = false;
            if (pausa) {
                reanudar();
            }
        }

        @Override
        public void run() {
            corriendo = true;

            while (corriendo) {
                actualizaFisica();
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

    }

    public ThreadJuego getThread() {
        return thread;
    }

}

