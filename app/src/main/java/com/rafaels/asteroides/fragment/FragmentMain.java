package com.rafaels.asteroides.fragment;

import android.app.Fragment;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rafaels.asteroides.activity.AcercaDe;
import com.rafaels.asteroides.activity.Juego;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuacionesArray;
import com.rafaels.asteroides.almacenPuntuaciones.AlmacenPuntuaciones;
import com.rafaels.asteroides.activity.PreferenciasActivity;
import com.rafaels.asteroides.activity.Puntuaciones;
import com.rafaels.asteroides.R;

/**
 * Created by Rafael S Martin on 18/10/2017.
 */

public class FragmentMain extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    private Button bAcercaDe, bSalir, bConfig, bPlay;
    public static AlmacenPuntuaciones almacen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        almacen= new AlmacenPuntuacionesArray();
//        launchActivity(Puntuaciones.class);

    }


    // Infla la vista del fragment con el XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        bAcercaDe = (Button) view.findViewById(R.id.button_about);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchActivity(AcercaDe.class);
            }
        });

        bSalir = (Button) view.findViewById(R.id.button_exit);
        bSalir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                launchActivity(Puntuaciones.class);
            }
        });

        bConfig = (Button) view.findViewById(R.id.button_config);
        bConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(PreferenciasActivity.class);
            }
        });

        bPlay = (Button) view.findViewById(R.id.button_start);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mostrarPreferencias();
                launchActivity(Juego.class);
            }
        });

        return view;
    }

    public void launchActivity(Class clase){
        Intent i = new Intent(getActivity(), clase);
        startActivity(i);
    }

    public void mostrarPreferencias(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String s = "música: " + pref.getBoolean("musica",true)
                +", gráficos: " + pref.getString("graficos","?")
                +", fragmentos: " + pref.getString("fragmentos","?")
                +", multijugador: " + pref.getBoolean("multijugador",true)
                +", jugadores: " + pref.getString("jugadores","?")
                +", conexion: " +pref.getString("conexion","?");
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
