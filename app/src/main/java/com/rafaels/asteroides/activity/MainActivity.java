package com.rafaels.asteroides.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rafaels.asteroides.fragment.FragmentMain;
import com.rafaels.asteroides.R;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    int pos;

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
        setContentView(R.layout.activity_main);

        showToast("onCreate");

        //Ocultar toolbar
//        getSupportActionBar().hide();

        mp = MediaPlayer.create(this, R.raw.juego);
        mp.start();

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new FragmentMain())
                .commit();



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

    public void launchActivity(Class clase){
        Intent i = new Intent(this, clase);
        startActivity(i);
    }

    public void showToast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
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
        mp.start();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        showToast("onResume");
        if(mp != null)
            mp.start();
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
