package edu.unlpam.vet.ponzonosos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GaleriaPagerAdapter extends RecyclerView.Adapter<GaleriaPagerAdapter.ViewHolder> {
    private final List<SpacePhoto> photos;
    private final Context context;

    public GaleriaPagerAdapter(Context context, List<SpacePhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.galeria_imagen, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpacePhoto photo = photos.get(position);
        Glide.with(context).load(photo.getUrl()).into(holder.imageView);

        // Al hacer clic, abrir SpacePhotoActivity
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpacePhotoActivity.class);
            intent.putExtra("photo", photo.getUrl());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View vista) {
            super(vista);
            imageView = vista.findViewById(R.id.iv_galeria);
        }
    }
}
