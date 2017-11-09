package com.rafaels.asteroides.graphics;

/**
 * Created by Rafael S Martin on 08/11/2017.
 */
import android.graphics.drawable.Drawable;
import android.view.View;

public class Misil extends Grafico {

    public static int PASO_VELOCIDAD_MISIL = 12;
    public static int TIEMPO_VIDA = 2;
    private boolean misilActivo = false;
    private int tiempoMisil;

    public Misil(View view, Drawable drawable) {
        super(view, drawable);

    }

    public static int getPASO_VELOCIDAD_MISIL() {
        return PASO_VELOCIDAD_MISIL;
    }

    public static void setPASO_VELOCIDAD_MISIL(int velocidadMisil) {
        PASO_VELOCIDAD_MISIL = velocidadMisil;
    }

    public boolean isMisilActivo() {
        return misilActivo;
    }

    public void setMisilActivo(boolean misilActivo) {
        this.misilActivo = misilActivo;
    }

    public int getTiempoMisil() {
        return tiempoMisil;
    }

    public void setTiempoMisil(int tiempoMisil) {
        this.tiempoMisil = tiempoMisil;
    }

}
