package com.rafaels.asteroides.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuaciones;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesArray;
import com.rafaels.asteroides.R;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesFicheroExtApl;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesFicheroExterno;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesFicheroInterno;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesGSon;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesJSon;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesPreferencias;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesProvider;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesRecursoAssets;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesRecursoRaw;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesSQLite;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesSQLiteRel;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesSocket;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesXML_DOM;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesXML_SAX;

public class MainActivity extends AppCompatActivity {

    public static MediaPlayer mp;
    int pos;
    private SharedPreferences pref;
    private String prefMusica, prefAlamacen;

    // Store instance variables
    private String title;
    private int page;
    private Button bAcercaDe, bSalir, bConfig, bPlay;
    public static AlmacenPuntuaciones almacen;
    private TextView textView;
    private Animation animation;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    //Puntuaciones del juego
    static final int ACTIV_JUEGO = 0;
    static final int PREF_ALMACENAMINETO = 10;

    //Volley
    public static RequestQueue colaPeticiones;
    public static ImageLoader lectorImagenes;

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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }


//        if(!getGrantedWriteExternalPermission()){
//            requestWriteExternalPermission(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//        }
//        else
//        {
//            //Aqui lo que quiera hacer
//
//        }

        colaPeticiones = Volley.newRequestQueue(this);
        //Asociado a un request
        lectorImagenes = new ImageLoader(colaPeticiones,
                //Para gestionar la cache
                new ImageLoader.ImageCache() {
                    //Almacena memoria a pares con maximo de elementos a 10
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                    //Recupera elementos en cache
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                    //Almacena elementos en cache
                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

        pref = PreferenceManager.getDefaultSharedPreferences(this);

//        almacen= new AlmacenPuntuacionesArray();
//        prefAlamacen = pref.getString("almacenamiento","?");
//
//        if(prefAlamacen.equals("0")){
//            almacen= new AlmacenPuntuacionesArray();
//        } else if(prefAlamacen.equals("1")){
//            almacen = new AlmacenPuntuacionesPreferencias(this);
//        } else if (prefAlamacen.equals("2")){
//            almacen = new AlmacenPuntuacionesFicheroInterno(this);
//        } else if (prefAlamacen.equals("3")){
//            almacen = new AlmacenPuntuacionesFicheroExterno(this);
//        }
//        Log.d("almacenamientoOnCreate", prefAlamacen);


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
//                Intent i = new Intent(getApplicationContext(), PreferenciasActivity.class);
//                startActivityForResult(i, PREF_ALMACENAMINETO);
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
            String nombre = "Rafael Martin";
            //Mejor leer nombre desde un AlertDialod.Builder o preferencias
            almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
            launchActivity(Puntuaciones.class);
        }
    }

    public boolean getGrantedWriteExternalPermission()
    {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWriteExternalPermission(int requestCode)
    {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
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
        prefAlamacen = pref.getString("almacenamiento","0");

        if(prefAlamacen.equals("0")){
            almacen= new AlmacenPuntuacionesArray();
        } else if(prefAlamacen.equals("1")){
            almacen = new AlmacenPuntuacionesPreferencias(this);
        } else if (prefAlamacen.equals("2")){
            almacen = new AlmacenPuntuacionesFicheroInterno(this);
        } else if (prefAlamacen.equals("3")){
            almacen = new AlmacenPuntuacionesFicheroExterno(this);
        } else if(prefAlamacen.equals("4")){
            almacen = new AlmacenPuntuacionesRecursoRaw(this);
        } else if(prefAlamacen.equals("5")){
            almacen = new AlmacenPuntuacionesRecursoAssets(this);
        } else if(prefAlamacen.equals("6")){
            almacen = new AlmacenPuntuacionesFicheroExtApl(this);
        } else if (prefAlamacen.equals("7")){
            almacen = new AlmacenPuntuacionesXML_SAX(this);
        } else if (prefAlamacen.equals("8")){
            almacen = new AlmacenPuntuacionesXML_DOM(this);
        } else if (prefAlamacen.equals("9")){
            almacen = new AlmacenPuntuacionesGSon();
        } else if(prefAlamacen.equals("10")){
            almacen = new AlmacenPuntuacionesJSon();
        } else if (prefAlamacen.equals("11")){
            almacen = new AlmacenPuntuacionesSQLite(this);
        } else if (prefAlamacen.equals("12")){
            almacen = new AlmacenPuntuacionesSQLiteRel(this);
        } else if (prefAlamacen.equals("13")){
            almacen = new AlmacenPuntuacionesProvider(this);
        } else if (prefAlamacen.equals("14")){
            almacen = new AlmacenPuntuacionesSocket();
        }
        Log.d("almacenamientoOnResume", prefAlamacen);

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
