package com.rafaels.asteroides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.rafaels.asteroides.fragment.FragmentMain;
import com.rafaels.asteroides.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ocultar toolbar
//        getSupportActionBar().hide();

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


}
