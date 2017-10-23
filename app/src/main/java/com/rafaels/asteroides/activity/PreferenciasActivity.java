package com.rafaels.asteroides.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rafaels.asteroides.fragment.PreferenciasFragment;

/**
 * Created by Rafael S Martin on 13/10/2017.
 */

public class PreferenciasActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}
