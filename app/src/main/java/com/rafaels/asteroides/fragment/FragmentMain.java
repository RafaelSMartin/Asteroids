package com.rafaels.asteroides.fragment;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rafaels.asteroides.activity.AcercaDe;
import com.rafaels.asteroides.activity.Juego;
import com.rafaels.asteroides.activity.MainActivity;
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
    private TextView textView;
    private Animation animation;

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

        textView = (TextView) view.findViewById(R.id.textView);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.giro_con_zoom);
//        textView.startAnimation(animation);

        bAcercaDe = (Button) view.findViewById(R.id.button_about);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.giro_con_zoom);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.parpadeo);
//        bAcercaDe.startAnimation(animation);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                bAcercaDe.startAnimation(animation);
                launchActivity(AcercaDe.class);
            }
        });

        bSalir = (Button) view.findViewById(R.id.button_exit);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_salir);
//        bSalir.startAnimation(animation);
        bSalir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                getActivity().finish();
//                mostrarPreferencias();
            }
        });

        bConfig = (Button) view.findViewById(R.id.button_config);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.desplazamiento_derecha);
//        bConfig.startAnimation(animation);
        bConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(PreferenciasActivity.class);
            }
        });

        bPlay = (Button) view.findViewById(R.id.button_start);
//        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.aparecer);
//        bPlay.startAnimation(animation);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        String sensor = "sensores" + pref.getBoolean("sensores", true);
        Toast.makeText(getActivity(), sensor, Toast.LENGTH_SHORT).show();
    }


    /**
     *
     * CICLO DE VIDA
     *
     * */

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}
