package com.rafaels.asteroides.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuaciones;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesArray;
import com.rafaels.asteroides.R;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesPreferencias;

public class MainActivity extends AppCompatActivity {

    public static MediaPlayer mp;
    int pos;
    private SharedPreferences pref;
    private String prefMusica;

    // Store instance variables
    private String title;
    private int page;
    private Button bAcercaDe, bSalir, bConfig, bPlay;
    public static AlmacenPuntuaciones almacen;
    private TextView textView;
    private Animation animation;

    //Puntuaciones del juego
    static final int ACTIV_JUEGO = 0;

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if(mp != null){
            pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null){
            pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

//        almacen= new AlmacenPuntuacionesArray();
        almacen = new AlmacenPuntuacionesPreferencias(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        prefMusica = "" + pref.getBoolean("musica", true);
        mp = MediaPlayer.create(this, R.raw.juego);

//        showToast("onCreate");

        //Ocultar toolbar
//        getSupportActionBar().hide();

        if(prefMusica.equals("true")){
            mp.start();
        } else{
            if (mp != null){
                mp.release();
                mp = null;
            }
        }

        textView = (TextView) findViewById(R.id.textView);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.giro_con_zoom);
//        textView.startAnimation(animation);

        bAcercaDe = (Button) findViewById(R.id.button_about);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.giro_con_zoom);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.parpadeo);
//        bAcercaDe.startAnimation(animation);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                bAcercaDe.startAnimation(animation);
                launchActivity(AcercaDe.class);
            }
        });

        bSalir = (Button) findViewById(R.id.button_exit);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_salir);
//        bSalir.startAnimation(animation);
        bSalir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                launchActivity(Puntuaciones.class);
//                mostrarPreferencias();
            }
        });

        bConfig = (Button) findViewById(R.id.button_config);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.desplazamiento_derecha);
//        bConfig.startAnimation(animation);
        bConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(PreferenciasActivity.class);
            }
        });

        bPlay = (Button) findViewById(R.id.button_start);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.aparecer);
//        bPlay.startAnimation(animation);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchActivity(Juego.class);
                Intent i = new Intent(getApplicationContext(), Juego.class);
                startActivityForResult(i, ACTIV_JUEGO);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asteroids, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            launchActivity(PreferenciasActivity.class);
            return true;
        }
        if (id == R.id.acercaDe) {
            launchActivity(AcercaDe.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIV_JUEGO && resultCode == RESULT_OK && data!=null){
            int puntuacion = data.getExtras().getInt("puntuacion");
            String nombre = "Yo";
            //Mejor leer nombre desde un AlertDialod.Builder o preferencias
            almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
            launchActivity(Puntuaciones.class);
        }
    }

    public void launchActivity(Class clase){
        Intent i = new Intent(this, clase);
        startActivity(i);
    }

    public void showToast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    public void mostrarPreferencias(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica",true)
                +", gráficos: " + pref.getString("graficos","?")
                +", fragmentos: " + pref.getString("fragmentos","?")
                +", multijugador: " + pref.getBoolean("multijugador",true)
                +", jugadores: " + pref.getString("jugadores","?")
                +", conexion: " +pref.getString("conexion","?");
        String sensor = "sensores" + pref.getBoolean("sensores", true);
        Toast.makeText(this, sensor, Toast.LENGTH_SHORT).show();
    }


    /**
     *
     * CICLO DE VIDA
     *
     * */

    @Override
    protected void onStart(){
        super.onStart();
//        showToast("onStart");
        if(mp != null)
            mp.start();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        showToast("onResume");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        prefMusica = "" + pref.getBoolean("musica", true);

        if(prefMusica.equals("true")){
            if(mp != null){
//                showToast("1-->"+prefMusica);
                mp.start();
            } else{
//                showToast("2-->"+prefMusica);
                mp = MediaPlayer.create(this, R.raw.juego);
                mp.start();
            }
        }
        if(prefMusica.equals("false")){
            if (mp != null){
//                showToast("3-->"+prefMusica);
                mp.release();
                mp = null;
            }
        }
    }

    @Override
    protected void onPause(){
//        showToast("onPause");
        if(mp !=null)
            mp.pause();
        super.onPause();
    }

    @Override
    protected void onStop(){
//        showToast("onStop");
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
//        showToast("onRestart");
    }

    @Override
    protected void onDestroy(){
//        showToast("onDestroy");
        if (mp != null){
            mp.release();
            mp = null;
        }
        super.onDestroy();
    }

}
