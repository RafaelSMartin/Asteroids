package com.rafaels.asteroides.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.rafaels.asteroides.R;
import com.rafaels.asteroides.activity.MainActivity;

import java.util.Vector;


public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.ViewHolder> {

    private LayoutInflater inflador;
    private Vector<String> lista;
    protected View.OnClickListener onClickListener;

    public MiAdaptador(Context context, Vector<String> lista) {
        this.lista = lista;
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.elemento_lista, parent, false);

        v.setOnClickListener(onClickListener);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.titulo.setText(lista.get(i));
        switch (Math.round((float) Math.random()*3)){
            case 0:
//                holder.icon.setImageResource(android.R.drawable.alert_dark_frame);
                //Carga de Imagenes con Volley
                MainActivity.lectorImagenes.get("http://mmoviles.upv.es/img/moviles.png",
                        ImageLoader.getImageListener(holder.icon, R.drawable.asteroide1,
                                R.drawable.asteroide3));
                break;
            case 1:
                holder.icon.setImageResource(android.R.drawable.star_on);
                break;
            case 2 :
                holder.icon.setImageResource(android.R.drawable.ic_delete);
                break;
            default:
                holder.icon.setImageResource(R.mipmap.ic_launcher);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, subtitutlo;
        public ImageView icon;
        ViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView)itemView.findViewById(R.id.titulo);
            subtitutlo = (TextView)itemView.findViewById(R.id.subtitulo);
            icon = (ImageView)itemView.findViewById(R.id.icono);
        }
    }
}
