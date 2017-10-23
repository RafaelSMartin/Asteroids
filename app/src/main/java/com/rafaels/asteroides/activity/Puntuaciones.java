package com.rafaels.asteroides.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.rafaels.asteroides.adapter.MiAdaptador;
import com.rafaels.asteroides.fragment.FragmentMain;
import com.rafaels.asteroides.R;


public class Puntuaciones extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MiAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptador = new MiAdaptador(this, FragmentMain.almacen.listaPuntuaciones(10));
        recyclerView.setAdapter(adaptador); layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= recyclerView.getChildAdapterPosition(v);
                String s = FragmentMain.almacen.listaPuntuaciones(10).get(pos);
                Toast.makeText(Puntuaciones.this, "Selección: " + pos + " - " + s, Toast.LENGTH_LONG).show();
            }
        });

    }
}
