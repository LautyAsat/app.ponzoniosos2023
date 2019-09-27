package edu.unlpam.vet.ponzonosos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.InfoAnimal;
import com.example.rodrimartin.ponzonosos.R;
import edu.unlpam.vet.ponzonosos.model.Imagen;
import java.util.List;

public class AdaptadorGrid extends BaseAdapter{
    private Context mContext;
    private List<Animal> animales;
    private static LayoutInflater inflater= null;

    public AdaptadorGrid(Context menu, List<Animal> listaAnimales) {
        mContext = menu;
        animales = listaAnimales;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return animales.size();
    }

    @Override
    public Object getItem(int posicion) {
        return posicion;
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int posicion, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View fila;
        fila = inflater.inflate(R.layout.item_grid, null);
        holder.tv= fila.findViewById(R.id.tv_gridnombre);
        holder.img= fila.findViewById(R.id.iv_gridimg);
        holder.tv.setText(animales.get(posicion).getNombre());
        Imagen principalImage = animales.get(posicion).getPrincipalImage();
        if (principalImage != null) {
            Glide.with(mContext)
                    .load(mContext.getFilesDir()+"/"+principalImage.getImg())
                    .into(holder.img);
        }
        fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,InfoAnimal.class);
                intent.putExtra("obj", animales.get(posicion).getId());
                mContext.startActivity(intent);
            }
        });
        return fila;
    }
}
